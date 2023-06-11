const express = require('express');
const dotenv = require('dotenv');
const connectDB = require('./db');
const app = express();

app.use(express.json({}));
app.use(express.json({
  extended: true
}));

dotenv.config();
connectDB();

app.use('/tweet', require('./routes/tweet'));

const { PORT } = process.env;
app.listen(PORT, 
  console.log(`Server running on port: ${PORT}`)
);