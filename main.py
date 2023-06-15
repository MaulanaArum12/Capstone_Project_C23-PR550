import os
import re
import datetime
import pandas as pd
import string
import itertools
import pymongo
import nltk
from nltk.corpus import stopwords
nltk.download("stopwords")
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.models import load_model
import snscrape.modules.twitter as sntwitter
from dotenv import load_dotenv
load_dotenv()

from flask import Flask
app = Flask(__name__)

def crawling_data(search):
  # Batasi jumlah hasil yang diambil
  max_results = 100

  # Tentukan nama file dengan format "<kueri pencarian>_<tanggal saat ini>.json"
  filename = f"{search.replace(' ', '')}.json"

  USING_TOP_SEARCH = True
  snscrape_params = '--jsonl --max-results'
  twitter_search_params = ''

  if USING_TOP_SEARCH:
      twitter_search_params += "--top"

  snscrape_search_query = f"snscrape {snscrape_params} {max_results} twitter-search {twitter_search_params} '{search}' > {filename}"
  os.system(snscrape_search_query)

  # Membaca file JSON hasil dari perintah CLI sebelumnya dan membuat dataframe pandas
  dfr = pd.read_json(filename, lines=True)
  if len(dfr) == 0:
    print('Pencarian tidak ditemukan coba ganti keyword lain, keywordmu: ', search)
    exit()

  # Membuat kamus untuk mengganti nama kolom
  new_columns = {
      'conversationId': 'Conv. ID',
      'url': 'URL',
      'date': 'Date',
      'rawContent': 'tweet',
      'id': 'ID',
      'replyCount': 'reply',
      'retweetCount': 'Retweets',
      'likeCount': 'like',
      'quoteCount': 'Quotes',
      'bookmarkCount': 'Bookmarks',
      'lang': 'Language',
      'links': 'Links',
      'media': 'Media',
      'retweetedTweet': 'Retweeted Tweet',
      'username': 'username'
  }

  # Memilih kolom yang akan digunakan dan mengganti nama kolom menggunakan kamus yang telah dibuat
  dfr = dfr.loc[:, ['username', 'rawContent', 'replyCount', 'likeCount']]
  dfr = dfr.rename(columns=new_columns)
  return dfr

  # # Creating list to append tweet data to
  # tweets = []

  # # Using TwitterSearchScraper to scrape data and append tweets to list
  # for i,tweet in enumerate(sntwitter.TwitterSearchScraper('jalan rusak').get_items()):
  #     if i>100:
  #         break
  #     tweets.append([tweet.username, tweet.content, tweet.replyCount, tweet.likeCount])
      
  # # Creating a dataframe from the tweets list above
  # df = pd.DataFrame(tweets, columns=['username', 'tweet', 'reply', 'like',])
  # return df

# Stopwords Function
def split_into_words(tweet):
  words = tweet.split()
  return words

def to_lower_case(words):
  words = [word.lower() for word in words]
  return words

def remove_punctuation(words):
  re_punc = re.compile('[%s]' % re.escape(string.punctuation))
  stripped = [re_punc.sub('', w) for w in words]
  return stripped

def remove_stopwords(words):
  stop_words = set(stopwords.words('indonesian'))
  stop_words1 = set(stopwords.words('english'))
  words = [w for w in words if not w in stop_words if not w in stop_words1]
  return words

def keep_alphabetic(words):
  words = [word for word in words if word.isalpha()]
  return words

def to_sentence(words):
  return ' '.join(words)

def tweet(words):
  tweet_tokenizer = nltk.tokenize.TweetTokenizer(strip_handles=True, reduce_len=True)
  tweet = tweet_tokenizer.tokenize(words)
  return tweet

def denoise_text(tweet):
  stop = set(stopwords.words('indonesian'))
  stop1 = set(stopwords.words('english'))
  punctuation = list(string.punctuation)
  stop.update(punctuation)
  stop1.update(punctuation)
  
  words = split_into_words(tweet)
  words = to_lower_case(words)
  words = remove_punctuation(words)
  words = keep_alphabetic(words)
  words = remove_stopwords(words)
  return to_sentence(words)

def clean_tokenizer(data):
  # Clean & Tokenizer Process
  data = data.apply(denoise_text)
  tokenizer = Tokenizer(num_words = 10000, oov_token = '<OOV>')
  tokenizer.fit_on_texts(data)
  test_sequences = tokenizer.texts_to_sequences(data)
  test_padded_sequences = pad_sequences(test_sequences, maxlen=50, padding='post', truncating='post')
  return test_padded_sequences

def predict(df, test_padded_sequences):
  # Model Predict
  model = load_model("model.h5")
  x = model.predict(test_padded_sequences)
  df_test_padded = pd.DataFrame({'accuracy': x.flatten()})
  data_list = df_test_padded.values.tolist()
  flat_list = list(itertools.chain(*data_list))
  int_list = [int(num * 100) for num in flat_list]
  df['asp_score'] = int_list
  return df

def result(df, data, search):
  # Modify Results
  df['clean'] = data.apply(denoise_text)
  column_order = ['username', 'tweet', 'clean', 'reply', 'like', 'asp_score']
  results = df.reindex(columns=column_order)
  results.sort_values(["asp_score"], axis=0, ascending=False,inplace=True)
  results.insert(5, 'topic', f"{search.replace(' ', '')}")
  results.insert(0, '_id', range(1, 1+len(results)))
  return results

def insert_db(results):
  # Insert/Update to MongoDB
  client = pymongo.MongoClient(os.getenv('MONGO_URI'))
  db = client['netweezen']
  col = db['tweets']

  data_dict = results.to_dict(orient='records')
  print("Insert/Update to MongoDB...")
  # col.insert_many(data_dict)
  for d in data_dict:
    col.replace_one({'_id':d['_id']}, d, upsert=True)
  return "Database updated"

@app.route("/netweezen-job")
def main():
  # df = pd.read_csv("jalanrusak.csv")
  search = "jalan rusak"
  df = crawling_data(search)
  data = df['tweet']
  test_padded_sequences = clean_tokenizer(data)
  df = predict(df, test_padded_sequences)
  results = result(df, data, search)
  insert_db(results)
  return "Finished executing."

if __name__ == "__main__":
  app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
