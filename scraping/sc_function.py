from bs4 import BeautifulSoup
from selenium import webdriver
from ..security import *

class Content :
    def __init__(self) :
        self.avg_rating = 0.0
        self.rating_num = 0
        self.review_num = 0

        self.open_operation = ''
        self.tag = ''
        self.intoroduce = ''

        self.menu = ''
        self.main_img = ''
        self.blog_link = []

def scraping_evalinfo(soup : BeautifulSoup, content : Content) :
    try : 
        eval = soup.find('div',{'class' : 'location_evaluation'})
    except AttributeError :
        return
    
    if eval : eval = eval.text
    else : return

    if "후기미제공" not in eval and "후기" in eval :
        content.avg_rating = float(eval[eval.find('후기 ')+3:eval.rfind('점')].strip())
        content.rating_num = int(eval[eval.rfind('(')+1:eval.rfind(')')])
    content.review_num = int(eval[eval.find('리뷰 ')+3:eval.rfind('개')].strip())
    return

def scraping_placeinfo(soup : BeautifulSoup, content : Content) :
    details_placeinfo = soup.find('div',{'class':'details_placeinfo'})
    if details_placeinfo :
        time_operation = details_placeinfo.find('div',{'class' : 'location_detail openhour_wrap'})
        if time_operation :
            open_operation = time_operation.find('div',{'class' : 'inner_floor'})
            if open_operation : content.open_operation = open_operation.find('ul').text.strip()
            else : 
                content.open_operation = time_operation.find('ul')
                if content.open_operation : content.open_operation = content.open_operation.text.strip()
                else : content.open_operation = ''

        tag = details_placeinfo.find('div','txt_tag')
        if tag : content.tag= tag.find('span','tag_g').text.replace('\n','').strip()
    
        intoroduce = details_placeinfo.find('p','txt_introduce')
        if intoroduce : content.intoroduce = intoroduce.text.strip()
    return

def scraping_menuinfo(soup : BeautifulSoup, content : Content) :
    menuinfo = soup.find('div',{'data-viewid' : 'menuInfo','class' : 'cont_menu'})
    if menuinfo : 
        content.menu = menuinfo.find('ul').text.replace('\n\n','').replace('\n\n\n','').strip()
    return

def scraping_photoinfo(soup : BeautifulSoup, content : Content) :
    photo_section = soup.find('div',{'data-viewid' : 'photoSection','class' : 'cont_photo no_category'})
    if photo_section :
        photo_section = photo_section.find('div','photo_area')
        main_img = photo_section.find('a','link_photo')
        if main_img : 
            main_img = main_img['style']
            content.main_img = main_img[main_img.find("'")+1:main_img.rfind("'")]
    return

def scraping_reviewinfo(soup : BeautifulSoup, content : Content) :
    blog_review = soup.find('div',{'class' : 'cont_review', 'data-viewid' : 'review'})
    if blog_review :
        blog_review = blog_review.find_all('a','link_review')
        content.blog_link = list(map(lambda a : a['href'], blog_review))
    return