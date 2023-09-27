FROM node AS frontend
WORKDIR /app
COPY package.json .
RUN npm i
COPY . .
RUN npm run build

FROM nginx:stable-alpine AS nginx
COPY --from=frontend /app/dist/frontend /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/*
COPY --from=frontend /app/nginx.conf.template /etc/nginx/conf.d/default.template

EXPOSE 80
CMD sh -c "envsubst \"`env | awk -F = '{printf \" \\\\$%s\", $1}'`\" < /etc/nginx/conf.d/default.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"
