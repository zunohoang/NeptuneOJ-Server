# Use an official Ubuntu as a parent image
FROM ubuntu:20.04

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Install necessary packages
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get install -y time \

# Create a user for running the code
RUN useradd -ms /bin/bash judge

# Switch to the judge user
USER judge

# Set the working directory
WORKDIR /home/judge

# Command to run the user's code
CMD ["BASH"]
