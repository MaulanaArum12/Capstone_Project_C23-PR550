FROM node:18-slim
 
WORKDIR /usr/app
COPY package.json package-lock.json ./
 
RUN npm install
COPY . .
RUN npm run build

CMD ["npm", "start"]