const mongoose = require('mongoose');

const TweetSchema = new mongoose.Schema({
  name: {
    type: String
  },
  username: {
    type: String
  },
  tweet: {
    type: String
  },
  clean_tweet: {
    type: String
  },
  like: {
    type: Number
  },
  reply: {
    type: Number
  },
  topic: {
    type: String
  },
  asp_score: {
    type: Number
  }
});

module.exports = Tweet = mongoose.model('tweet', TweetSchema);