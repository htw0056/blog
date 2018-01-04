# ncdc数据获取

因为hadoop权威指南中需要ncdc的数据作为demo，所以直接用python脚本获取了批量的数据。


```python
#! /usr/bin/python2.7
# -*- coding:utf-8 -*-

import ftplib
import os


def login():
    ftp = ftplib.FTP()
    ftp.set_debuglevel(2)
    ftp.connect('ftp.ncdc.noaa.gov', 21)
    ftp.login('', '')
    return ftp


def down():
    ftp = login()
    path = '/pub/data/noaa/'
    os.mkdir('data')

    for year in range(1901, 1906):  # year
        os.mkdir('data/' + str(year))
        list = ftp.nlst(path + str(year))
        # print(list)
        for l in list:
            filename = l.split('/')[-1]
            file = open('data/' + str(year) + '/' + filename, 'wb')
            ftp.retrbinary('RETR ' + path + str(year) + '/' + filename, file.write, 1024)
            file.close()

    ftp.quit()


if __name__ == '__main__':
    down()
```