#encoding=utf-8
from __future__ import unicode_literals
import json
import sys
import sqlite3
import os
import httplib

reload(sys)
sys.setdefaultencoding('utf-8')

def parseGames():
    all = json.loads(open("games.txt", 'r').read())
    # print type(all)
    games = all["data"]
    print len(games)

    db = sqlite3.connect("wanke.sqlite3.sqlite")

    gameList = []

    index = 0
    for game in games:
        gameId = game["cate_id"].strip()
        gameName = game["game_name"].strip()
        gameShortName = game["short_name"].strip()
        gameCover = game["game_src"].strip()
        gameIcon = game["game_icon"].strip()
        gameList.append(game)

        print "处理", gameName, index, gameId
        index += 1

        cover = ""
        if len(os.path.split(gameCover)) > 1:
            cover = os.path.split(gameCover)[1]

        icon = ""
        if len(os.path.split(gameIcon)) > 1:
            icon = os.path.split(gameIcon)[1]

        t = (index, gameId, gameName, gameShortName, cover, icon)
        db.execute("insert into games values (?,?,?,?,?,?)", t)

        print "  获取cover", gameCover
        getFile(gameCover, "cover")
        print "  获取icon", gameIcon
        getFile(gameIcon, "icon")
        print 
        # raw_input()
    db.commit()

def getFile(url, destDir="."):
    if not url.startswith("http"):
        return

    if not os.path.isdir(destDir):
        os.makedirs(destDir)

    target = os.path.join(destDir, os.path.split(url)[1])
    command = "wget "+url+" -O "+ target
    print command
    print os.system(command)
    

def parseLiveChannels():
    # http://api.douyutv.com/api/client/live?client_sys=android&offset=0&limit=20
    conn = httplib.HTTPConnection("api.douyutv.com")
    conn.request(method="POST", url="/api/client/live?client_sys=android&offset=0&limit=20")
    response = conn.getresponse();
    if response.status == 200:
        print "获取直播房间列表成功！"
        all = json.loads(response.read())
        rooms = all.get("data")
        # for room in 

# parseGames()
"""
获取推荐页顶部广告栏信息
"""
def parseAds():
    #http://api.douyutv.com/api/client/slide/4?client_sys=android
    conn = httplib.HTTPConnection("api.douyutv.com");      
    conn.request(method="POST", url="/api/client/slide/4?client_sys=android");      
    response = conn.getresponse()

    #判断是否提交成功     
    if response.status == 200:      
        print "发布成功!^_^!";      
    else:      
        print "发布失败\^0^/";      
    all = json.loads(response.read())
    ads = all.get("data")

    db = sqlite3.connect("wanke.sqlite3.sqlite")
    index = 0
    for ad in ads:
        adId = ad.get("id")
        adTitle = ad.get("title")
        adCover = ad.get("pic_url")
        adRoomId = ad.get("room").get("room_id")

        t = (index, adId, adTitle, os.path.split(adCover)[1], adRoomId)
        db.execute("insert into ads values (?,?,?,?,?)", t)

        getFile(adCover, "cover")
        index += 1

    db.commit()
    conn.close();

# parseAds()

"""
获取最热的视频直播列表
"""
def parseHot():
    # http://api.douyutv.com/api/client/live/2/?offset=0&limit=4&client_sys=android
    db = sqlite3.connect("wanke.sqlite3.sqlite")
    cursor = db.cursor() 
    cursor.execute("select gameId from games")
    gameIds = []
    for row in cursor.fetchall():
        gameIds.append(row[0])

    conn = httplib.HTTPConnection("api.douyutv.com")
    index = 0
    for gameId in gameIds:
        url = "/api/client/live/"+str(gameId)+"/?offset=0&limit=50&client_sys=android"
        print url
        conn.request(method="POST", url=url)
        response = conn.getresponse()
        # print response.status
        # print response.reason
        # raw_input()
        if response.status == 200:
            all = json.loads(response.read())
            rooms = all.get("data")
            for room in rooms:
                roomId = room.get("room_id")
                roomCover = room.get("room_src")
                roomName = room.get("room_name")
                tags = ""
                gameId = room.get("cate_id")
                gameName = room.get("game_name")
                vodQuality = 0
                showTime = room.get("show_time")
                ownerId = room.get("owner_uid")
                ownerAvatar = ""
                ownerNickname = room.get("nickname")
                online = room.get("online")
                fans = room.get("fans")
                detail = ""
                t = (index, roomId, roomName, os.path.split(roomCover)[1], tags, gameId, gameName, vodQuality, showTime, ownerId, ownerAvatar, ownerNickname, online,fans, detail)
                try:
                    db.execute("insert into live_channels values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", t)
                    index += 1
                except Exception, e:
                    print e

                getFile(roomCover, "cover")

                # 获取头像
                getFile("http://uc.douyutv.com/avatar.php?uid=%s&size=big"%ownerId, "cover")
            db.commit()
            # raw_input()
    cursor.close()
    db.close()

# parseHot()

def getDetail():
    db = sqlite3.connect("wanke.sqlite3.sqlite")
    cursor = db.cursor() 
    cursor.execute("select roomId from live_channels")
    roomIds = []
    for row in cursor.fetchall():
        roomIds.append(row[0])

    conn = httplib.HTTPConnection("api.douyutv.com")
    index = 0
    for roomId in roomIds:
        url = "/api/client/room/"+str(roomId)+"?client_sys=android"
        print url
        conn.request(method="POST", url=url)
        response = conn.getresponse()
        # print response.status
        # print response.reason
        # raw_input()
        if response.status == 200:
            all = json.loads(response.read())
            room = all.get("data")
            # roomCover = room.get("room_src")
            roomDetail = room.get("show_details")

            print roomId, roomDetail
            cmd = "update live_channels set detail='%s' where roomId=%d"%(roomDetail, roomId)
            # db.execute("update live_channels set detail=? where roomId=19574", (roomDetail))
            try:
                db.execute(cmd)
            except Exception, e:
                print e

    db.commit()


def ggg():
    for i in range(609):
        format = "http://img.zxshuo.cn/image/secret/album_%d.png"%i
        getFile(format)
        # raw_input()

ggg()