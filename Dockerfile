# dockerbuild/autochirp
FROM debian:latest
MAINTAINER Philip Schildkamp <philip.schlildkamp@lmu.de>


# sanity
ENV \
  DEBIAN_FRONTEND="noninteractive" \
  LANG="C.UTF-8" \
  LANGUAGE="C.UTF-8" \
  LC_ALL="C.UTF-8"


# configuration
ENV \
  AUTOCHIRP_PATH="/autochirp" \
  BACKPORTS_REPO="deb http://http.debian.net/debian jessie-backports main contrib non-free" \
  APT_PACKAGES=" \
    ca-certificates \
    maven \
    openjdk-8-jdk \
    "


# environment
RUN \
  echo ${BACKPORTS_REPO} >> /etc/apt/sources.list \
  && apt-get update \
  && apt-get -y --no-install-recommends install ${APT_PACKAGES}


# build
COPY ./ ${AUTOCHIRP_PATH}/
VOLUME ${AUTOCHIRP_PATH}/target
CMD \
  cd ${AUTOCHIRP_PATH} \
  && mvn package war:war
