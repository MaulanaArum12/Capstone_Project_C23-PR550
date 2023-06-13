const express = require ('express');
const Tweet = require('../models/Tweet');
const router = express.Router();

router.get('/', async (req, res) => {
  res.json({status: 'Netweezen! Ready to roll.'});
})

// GET Tweets by Topic
router.get('/:topic', async (req, res, next) => {
  try {
    const tweets = await Tweet.find({topic: req.params.topic});

    if (!tweets) {
      return res.status(400).json({
        success: false,
        msg: 'Something went wrong'
      });
    }

    res.status(200).json({
      success: true,
      count: tweets.length,
      tweets: tweets,
      msg: 'Successfully Get Tweets by Topic'
    });
  } 
  catch (error) {
    next(error);
  }
});

//GET Tweet Details by Topic & ID
router.get('/:topic/:id', async (req, res, next) => {
  try {
    const tweet = await Tweet.find({topic: req.params.topic, _id: req.params.id});

    if (!tweet) {
      return res.status(400).json({
        success: false,
        msg: 'Something went wrong'
      });
    }

    res.status(200).json({
      success: true,
      tweet: tweet,
      msg: 'Successfully Get Tweet Details'
    });
  } 
  catch (error) {
    next(error);
  }
});

module.exports = router;
