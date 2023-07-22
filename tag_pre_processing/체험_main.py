import pymysql
from ..security import *

캔들_향수_비누_tagList = ['캔들공방','향수공방','비누공방','수제비누','캔들클래스']

미술_tagList = ['팝아트','체험미술','취미미술','수채화','성인취미미술','미술공방','드로잉클래스','드로잉카페','전사지공방'
              ,'이태원유화','강동취미미술','서초화실','홍대미술공방']

요리_tagList = ['쿠킹클래스','케익공방','한식디저트공방','케이크주문제작','수제초콜릿','떡케이크주문제작','베이킹클래스','베이킹공방'
              ,'수제디저트','앙금플라워케이크','화과자공방']
요리_ban_tagList = ['디저트카페','애견수제간식']

금속_유리_tagList = ['쥬얼리클래스','은공예','반지만들기체험','반지공방','반지만들기','은점토공예','칠보공예','주얼리','은공방','금속공예'
                 ,'유리공예','유리공방','스테인글라스공예','레진공예','강남악세사리공방','서울주얼리공방','영등포반지공방']

가죽_tagList = ['가죽공예','가죽공방']

목공_tagList = ['목공체험','목공방','목공예','가구공방']

식물_tagList = ['플라워클래스','플라워공방','테라리움','꽃꽂이수업']

켈리그라피_tagList = ['켈리그라피','패브릭아트','캘리그라피클래스']

포장_tagList = ['선물포장','보자기클래스','보자기']

미니어처_tagList = ['미니어처클래스','미니어쳐','미니어처체험']

라탄_tagList = ['라탄공방','마포라탄공방','라탄공예']

뜨개질_tagList = ['뜨개공방','뜨개질공방','인형공방']

도자기_tagList = ['도자기공방','도예공방','도자공예','도예체험','도자기클래스','도자기']

터프팅_tagList = ['터프팅','터프팅공방']


def experience_category(tag) :

    tag = set(tag.split('#'))
    result = '체험'

    # 순서 중요
    if tag.intersection(캔들_향수_비누_tagList) : result = '캔들&향수&비누'
    if tag.intersection(미술_tagList) : result =  '미술'
    if tag.intersection(요리_tagList) and not tag.intersection(요리_ban_tagList) : result = '요리'
    if tag.intersection(금속_유리_tagList) : result = '금속&유리'
    if tag.intersection(가죽_tagList) : result = '가죽'
    if tag.intersection(목공_tagList) : result = '목공'
    # 순서 안중요
    if tag.intersection(식물_tagList) : result = '식물'
    if tag.intersection(켈리그라피_tagList) : result = '켈라그라피'
    if tag.intersection(포장_tagList) : result = '포장'
    if tag.intersection(미니어처_tagList) : result = '미니어처'
    if tag.intersection(라탄_tagList) : result = '라탄'
    if tag.intersection(뜨개질_tagList) : result = '뜨개질'
    if tag.intersection(도자기_tagList) : result = '도자기'
    if tag.intersection(터프팅_tagList) : result = '터프팅'
    return result
    



if __name__ == '__main__':
    
    category_id_tList= []

    db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
    try:
        with db.cursor() as cursor:
            sql = "SELECT id,tag FROM map_detail WHERE user_category_name = '체험'"
            cursor.execute(sql)
            result = cursor.fetchall()
        
    finally:
        db.close()

    for experience in result :
        category_id_tList.append((experience_category(experience[1]),experience[0]))
    
    db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
    try :
        with db.cursor() as cursor :
            sql = """UPDATE map_detail
                        SET user_category_name = %s
                        WHERE id=%s"""
            cursor.executemany(sql,category_id_tList)
            db.commit()

            sql = """UPDATE map
                        SET user_category_name = %s
                        WHERE id=%s"""
            cursor.executemany(sql,category_id_tList)
            db.commit()
    finally :
        db.close()

    