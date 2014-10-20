import json

"""
获取推荐页顶部互动的广告信息
live/ads
"""
def ads():
    parseRequest()
    db = DAL('sqlite://wanke.sqlite3.sqlite')
    allAds = db.executesql("select * from ads order by _id")
    result = {}
    result["error"] = 0

    jsonAds = []
    for ad in allAds:
        jsonAd = {}
        jsonAd["id"] = ad[1]
        jsonAd["title"] = ad[2]
        jsonAd["cover"] = ad[3]
        jsonAd["roomId"] = ad[4]
        jsonAds.append(jsonAd)

    result["data"] = jsonAds

    return json.dumps(result)


"""
获取当前支持直播的游戏列表
"""
def games():
    db = DAL('sqlite://wanke.sqlite3.sqlite')
    allGames = db.executesql("select * from games order by gameIndex")
    result = {}
    result["error"] = 0
    jsonGames = []
    for game in allGames:
        jsonGame = {}
        jsonGame["gameId"] = game[1]
        jsonGame["gameName"] = game[2]
        jsonGame["gameCover"] = game[4]
        jsonGames.append(jsonGame)

    result["data"] = jsonGames
    return json.dumps(result)

"""
http://api.douyutv.com/api/client/live/3/?offset=0&limit=4&client_sys=android
获取某款游戏的热门直播列表
"""
def recommend():
    """
    recommend?gameId=2&offset=0&limit=4
    获取room id的第offset页，每页4个
    limit  默认值为20
    offset 默认值为0
    gameId 如果没有，不按照游戏进行分类返回
    """
    parseRequest()
    gameId = request.vars.get("gameId", "")
    limit = int(request.vars.get("limit", 20))
    offset = int(request.vars.get("offset", 0))

    debug = request.vars.get("debug", "")
    if len(debug) > 0:
        gameId = ""

    db = DAL('sqlite://wanke.sqlite3.sqlite')
    allRooms = []
    if len(gameId) == 0:
        # 返回所有游戏中最热门的直播频道
        allRooms = db.executesql("select * from live_channels order by _id")
    else:
        sql = "select * from live_channels where gameId=%s order by _id"%gameId
        allRooms = db.executesql(sql)

    result = {}
    result["error"] = 0

    index = 0
    jsonRooms = []
    for room in allRooms:
        if index < limit*offset:
            index += 1
            continue

        if index >= limit*(offset+1):
            break

        jsonRoom = {}
        jsonRoom["roomId"] = room[1]
        jsonRoom["roomName"] = room[2]
        jsonRoom["roomCover"] = room[3]
        jsonRoom["gameId"] = room[5]
        jsonRoom["gameName"] = room[6]
        jsonRoom["ownerNickname"] = room[11]
        jsonRoom["online"] = room[12]
        jsonRoom["fans"] = room[13]
        jsonRooms.append(jsonRoom)

        index += 1

    result["data"] = jsonRooms
    return json.dumps(result)

def channel():
    """
    channel?roomId=2121
    roomId 如果没有，返回空
    """
    parseRequest()
    roomId = request.vars.get("roomId", "")

    db = DAL('sqlite://wanke.sqlite3.sqlite')
    allRooms = []

    result = ""
    if len(roomId) > 0:
        sql = "select * from live_channels where roomId=%s order by _id"%roomId
        allRooms = db.executesql(sql)

    room = None
    if len(allRooms) >= 1:
        room = allRooms[0]

    if room != None:
        jsonRoom = {}
        jsonRoom["roomId"] = room[1]
        jsonRoom["roomName"] = room[2]
        jsonRoom["roomCover"] = room[3]
        jsonRoom["gameId"] = room[5]
        jsonRoom["gameName"] = room[6]
        jsonRoom["ownerUid"] = room[9]
        jsonRoom["ownerNickname"] = room[11]
        jsonRoom["online"] = room[12]
        jsonRoom["fans"] = room[13]
        jsonRoom["detail"] = room[14]
        result = json.dumps(jsonRoom)

    return result

import os
"""
获取当前版本更新信息
"""
def version():
    parseRequest()
    platform = request.vars.get("type", "")

    result = {}
    result["error"] = 0

    if len(platform) == 0:
        return

    result["error"] = 0

    result["version"] = "1.0.1"
    result["forceUpdate"] = False
    result["updateShortLog"] = "这是一次假的更新。\nhaha ~~"
    result["updateDetailLog"] = "http://www.baidu.com"
    result["downUrl"] = "http://www.baidu.com"

    return json.dumps(result)

"""
获取图片
"""
def imgfile():
    filename = request.vars.get("id", "")
    if len(filename) == 0:
        return ""

    print filename
    # redirect(URL("/static/images/cover/"+filename))
    filepath = os.path.join(os.getcwd(), "applications")
    filepath = os.path.join(filepath, "wanketv")
    filepath = os.path.join(filepath, "static")
    filepath = os.path.join(filepath, "images")
    filepath = os.path.join(filepath, "cover")
    filepath = os.path.join(filepath, filename)
    url = "http://192.168.41.101:9257/wanketv/static/images/cover/"+filename
    redirect(url)

def parseRequest():
    # 发起请求客户端的操作系统类型
    platform = ""
    if request.vars.has_key("platform"):
        platform = request.vars.get("platform")

    # 发起请求客户端的操作系统版本
    os = ""
    if request.vars.has_key("os"):
        os = request.vars.get("os")

    # 发起请求客户端的版本
    version = ""
    if request.vars.has_key("version"):
        version = request.vars.get("version")

    token = ""
    if request.vars.has_key("token"):
        token = request.vars.get("token")

    return platform, os, version, token
