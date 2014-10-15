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
    jsonGames = []
    for game in allGames:
        jsonGame = {}
        jsonGame["gameId"] = game[1]
        jsonGame["gameName"] = game[2]
        jsonGame["gameCover"] = game[4]
        jsonGames.append(jsonGame)

    result["games"] = jsonGames
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
    limit = request.vars.get("limit", 20)
    offset = request.vars.get("offset", 0)

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
        if index >= limit:
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

    result["data"] = jsonRooms
    return json.dumps(result)




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
