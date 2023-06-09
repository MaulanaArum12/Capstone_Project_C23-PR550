const express = require ('express');
const Tweet = require('../models/Tweet');
const router = express.Router();

router.put('/:topic/:id', async (req, res, next) => {
  try {
    let tweet = await Tweet.find({topic: req.params.topic, id: req.params.id});
    if (!tweet) {
      return res.status(200).json({ success: false, msg: 'Tweet not exists'});
    }

    tweet = await Tweet.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
      runValidators: true
    });

    res.status(200).json({ success: true, tweets: tweet, msg: 'Successfully updated' });

  } catch (error) {
    next(error);
  }
});

router.post('/', async (req, res, next) => {
  try {
    const tweet = await Tweet.create({id: req.body.id, name: req.body.name, tweet: req.body.tweet, topic: req.body.topic});

    if (!tweet) {
      return res.status(400).json({
        success: false,
        msg: 'Something went wrong'
      });
    }

    res.status(200).json({
      success: true,
      tweet: tweet,
      msg: 'Successfully'
    });

  } catch (error) {
    next(error);
  }
});

// GET Tweet by Topic
router.get('/:topic', async (req, res, next) => {
  try {
    const tweet = await Tweet.find({topic: req.params.topic});

    if (!tweet) {
      return res.status(400).json({
        success: false,
        msg: 'Something went wrong'
      });
    }

    res.status(200).json({
      success: true,
      count: tweet.length,
      tweets: tweet,
      msg: 'Successfully'
    });

  } catch (error) {
    next(error);
  }
});

//GET Tweet Details
router.get('/:topic/:id', async (req, res, next) => {
  try {
    const tweet = await Tweet.find({topic: req.params.topic, id: req.params.id});

    if (!tweet) {
      return res.status(400).json({
        success: false,
        msg: 'Something went wrong'
      });
    }

    res.status(200).json({
      success: true,
      tweet: tweet,
      msg: 'Successfully'
    });

  } catch (error) {
    next(error);
  }
});

module.exports = router;
