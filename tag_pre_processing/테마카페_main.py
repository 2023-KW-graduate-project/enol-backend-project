import pymysql
from ..security import *

플라워카페_tagList = ['플라워카페','식물카페']

한옥카페_tagList = ['한옥카페']

슬라임카페_tagList = ['슬라임카페']
슬라임카페_ban_tagList = ['홍대공방']

파충류카페_tagList = ['파충류']

피로회복카페_tagList = ['마사지카페','안마의자카페','족욕카페']

뮤직카페2_tagList = ['뮤직카페','LP바']

타로_사주_상담카페2_tagList = ['심리상담카페','타로카페','사주카페']

드로잉카페_tagList = ['드로잉카페','홍대드로잉카페']

고양이카페2_tagList = ['고양이카페']

def experience_category(tag) :

    tag = set(tag.split('#'))
    result = '테마카페'

    # 순서 중요
    if tag.intersection(플라워카페_tagList) : result = '플라워카페'
    if tag.intersection(한옥카페_tagList) : result =  '한옥카페'
    if tag.intersection(슬라임카페_tagList) and not set(tag).intersection(슬라임카페_ban_tagList) : result = '슬라임카페'
    # 순서 안중요
    if tag.intersection(파충류카페_tagList) : result = '파충류카페'
    if tag.intersection(피로회복카페_tagList) : result = '피로회복카페'
    if tag.intersection(뮤직카페2_tagList) : result = '뮤직카페'
    if tag.intersection(타로_사주_상담카페2_tagList) : result = '타로&사주&상담카페'
    if tag.intersection(드로잉카페_tagList) : result = '드로잉카페'
    if tag.intersection(고양이카페2_tagList) : result = '고양이카페'
    return result
    



if __name__ == '__main__':
    
    category_id_tList= []

    db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
    try:
        with db.cursor() as cursor:
            sql = "SELECT id,tag FROM map_detail WHERE user_category_name = '테마카페'"
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

    