import pymysql
import time
from multiprocessing import Pool
from bs4 import BeautifulSoup
from selenium import webdriver
from ..security import *
from .sc_function import *

from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


def scraping(result) :
        content = Content()
        placeID = result[0]
        userCategoryCode = result[1]
        URL = 'https://place.map.kakao.com/' + str(placeID)

        options = webdriver.ChromeOptions()
        options.add_argument("--disable-extensions")
        options.add_argument('headless')
        options.add_argument('log-level=3')
        options.add_experimental_option("excludeSwitches", ["enable-logging"])
        options.add_argument("disable-gpu")
        user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
        options.add_argument('user-agent=' + user_agent)
        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()),options=options)
        driver.get(url=URL)

        for i in range(0,5) : 
            try:
                element = WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.CSS_SELECTOR , '#mArticle > div.cont_essential > div.details_placeinfo > h3')))
                break
            except :
                driver.refresh()

        page_source = driver.page_source
        soup = BeautifulSoup(page_source,'html.parser').select_one('#mArticle')

        scraping_evalinfo(soup,content)
        if content.avg_rating == 0 and content.review_num == 0 : 
            driver.close()
            return None
        scraping_placeinfo(soup,content)
        scraping_menuinfo(soup,content)
        scraping_photoinfo(soup,content)
        scraping_reviewinfo(soup,content)
        driver.close()

        data = dict()
        data['id']=placeID
        data['user_category_name']=userCategoryCode
        data['avg_rating']=content.avg_rating
        data['rating_num']=content.rating_num
        data['review_num']=content.review_num
        data['introduce']=content.intoroduce
        data['open_operation']=content.open_operation
        data['menu']=content.menu
        data['main_img']=content.main_img
        data['tag']=content.tag
        data['blog_link']=str(content.blog_link)
    
        return data


if __name__ == '__main__':
    total_start_time = time.time() ###test

    db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
    try:
        with db.cursor() as cursor:
            sql = """
                    SELECT id, user_category_name FROM map
                """ 
            cursor.execute(sql)
            result = cursor.fetchall()
        
    finally:
        db.close()

    pool = Pool(processes=12)
    INSERT_dictList = pool.map(scraping,result)
    pool.close()
    pool.join()
    
    INSERT_dictList = list(filter(lambda a : True if a is not None else False, INSERT_dictList))

    db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db=DB.get_db(),charset=DB.get_charset())
    try:
        with db.cursor() as cursor:
            sql = """INSERT INTO map_detail(id, user_category_name, avg_rating, rating_num, review_num, introduce, open_operation, menu, main_img, tag, blog_link)
                        VALUES (%(id)s, %(user_category_name)s, %(avg_rating)s, %(rating_num)s, %(review_num)s, %(introduce)s, %(open_operation)s, 
                                %(menu)s, %(main_img)s, %(tag)s, %(blog_link)s)"""
            cursor.executemany(sql,INSERT_dictList)
            db.commit()
    finally:
        db.close()

    print(time.time() - total_start_time) ###test
    




