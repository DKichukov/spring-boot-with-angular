 ### v1 nginx.conf file excluded from Dockerfile ###
# Stage 1: Build
FROM node:20.16.0-slim AS node-builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --output-path=./dist/out --configuration production

# Stage 2: Serve
FROM nginx:stable-alpine3.20-perl
COPY --from=node-builder /app/dist/out /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

#### v2 nginx.conf file included in Dockerfile ###
## Stage 1: Build
#FROM node:20.16.0-slim AS node-builder
#WORKDIR /app
#COPY package*.json ./
#RUN npm install
#COPY . .
#RUN npm run build -- --output-path=./dist/out --configuration production
#
## Stage 2: Serve
#FROM nginx:alpine
#COPY --from=node-builder /app/dist/out /usr/share/nginx/html
#
## Create nginx.conf directly in the Dockerfile
#RUN echo "server {" > /etc/nginx/conf.d/default.conf && \
#    echo "    listen 80;" >> /etc/nginx/conf.d/default.conf && \
#    echo "    server_name localhost;" >> /etc/nginx/conf.d/default.conf && \
#    echo "    location / {" >> /etc/nginx/conf.d/default.conf && \
#    echo "        root /usr/share/nginx/html;" >> /etc/nginx/conf.d/default.conf && \
#    echo "        index index.html index.htm;" >> /etc/nginx/conf.d/default.conf && \
#    echo "        try_files \$uri \$uri/ /index.html;" >> /etc/nginx/conf.d/default.conf && \
#    echo "    }" >> /etc/nginx/conf.d/default.conf && \
#    echo "    error_page 500 502 503 504 /50x.html;" >> /etc/nginx/conf.d/default.conf && \
#    echo "    location = /50x.html {" >> /etc/nginx/conf.d/default.conf && \
#    echo "        root /usr/share/nginx/html;" >> /etc/nginx/conf.d/default.conf && \
#    echo "    }" >> /etc/nginx/conf.d/default.conf && \
#    echo "}" >> /etc/nginx/conf.d/default.conf
#
#EXPOSE 80
#CMD ["nginx", "-g", "daemon off;"]
