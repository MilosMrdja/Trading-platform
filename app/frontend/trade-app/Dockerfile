FROM node:22.19-alpine as build

WORKDIR /app

COPY package*.json ./

RUN npm install
RUN npx ngcc --properties es2023 browser module main --first-only --create-ivy-entry-ponits

COPY . .
RUN npm run build

FROM nginx:stable
COPY --from=build /app/dist/trade-app/browser/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

