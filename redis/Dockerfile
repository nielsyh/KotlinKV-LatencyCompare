# Use the official Redis image as the base image
FROM i386/redis:latest
#FROM redis:latest

# Copy your custom Redis configuration file to the container
COPY redis.conf /usr/local/etc/redis/redis.conf

# Expose Redis default port
EXPOSE 6379
