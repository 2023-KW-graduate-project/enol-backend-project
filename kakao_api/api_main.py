from ..security import *
from .api_function import *
import pymysql

#서울 좌극하단 좌표
start_x = 126.734086
start_y = 37.413294

#서울 우극상단 좌표
end_x = 127.269311
end_y = 37.715133

next_x = 0.01
next_y = 0.01
num_x = 54
num_y = 31

# 1차
keyword = '서울'
category_group_code = 'FD6'
overlapped_result = overlapped_data(keyword, category_group_code, start_x, start_y, next_x, next_y, num_x, num_y)

# # 2차
# keyword = '서울 멀티'
# category_group_code = ''
# overlapped_result2 = overlapped_data(keyword, category_group_code, start_x, start_y, next_x, next_y, num_x, num_y)

# # 3차
# keyword = '서울 멀티룸카페'
# category_group_code = ''
# overlapped_result3 = overlapped_data(keyword, category_group_code, start_x, start_y, next_x, next_y, num_x, num_y)

# # OR
# overlapped_result.extend(overlapped_result2)
# overlapped_result.extend(overlapped_result3)
overlapped_result = list(map(dict, set(tuple(sorted(d.items())) for d in overlapped_result)))


#DB 저장
db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
try :
    with db.cursor() as cursor :
        sql = """INSERT INTO test_table(id, user_category_name,place_name, category_name, category_group_code, category_group_name, 
                                    address_name, road_address_name, x, y, place_url, phone)
                    VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"""
        val = list(map(lambda a: (a['id'],'음식점',a['place_name'],a['category_name'],a['category_group_code'],a['category_group_name'],
                                  a['address_name'],a['road_address_name'],a['x'],a['y'],a['place_url'],a['phone']) ,overlapped_result))
        cursor.executemany(sql,val)
        db.commit()
finally :
    db.close()
