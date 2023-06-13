const express = require('express');
const dotenv = require('dotenv');
const connectDB = require('./db');
const app = express();
const router = express.Router();

app.use(express.json({}));
app.use(express.json({
  extended: true
}));

dotenv.config();
connectDB();

router.get('/', async (res) => {
  res.json('Netweezen! Ready to roll.');
})
app.use('/tweets', require('./routes/tweet'));

const PORT = process.env.PORT || 8080;
app.listen(PORT, 
  console.log(`Server running on port: ${PORT}`)
);