# Fetching the latest node image on apline linux
FROM node:alpine AS builder

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY app/package.json ./
COPY app/yarn.lock ./
RUN yarn install --frozen-lockfile
COPY app/ ./
RUN yarn run build

# production environment
FROM nginx:stable-alpine-slim
COPY --from=builder /app/build /usr/share/nginx/html
# new
COPY app/nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]