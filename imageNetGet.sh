#! /bin/bash

url=`grep ^$1[^0-9] fall11_urls.txt | cut -f2`
wget ${url} -O img/$1.jpg
