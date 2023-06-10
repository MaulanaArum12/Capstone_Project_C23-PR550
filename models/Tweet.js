const mongoose = require('mongoose');

const tweetSchema = new mongoose.Schema({
  _id: {
    type: Number
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

module.exports = mongoose.model('Tweet', tweetSchema);