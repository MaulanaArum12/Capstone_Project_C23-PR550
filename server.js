const express = require('express');
const dotenv = require('dotenv');
const connectDB = require('./config/db');
const app = express();

app.use(express.json({}));
app.use(express.json({
  extended: true
}));

dotenv.config({
  path: './config/config.env'
});

connectDB();

app.use('/api/tweet', require('./routes/tweet'));

const PORT = process.env.PORT || 8080;
app.listen(PORT, 
  console.log(`Server running on port: ${PORT}`)
);