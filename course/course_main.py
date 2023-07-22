import pymysql
from ..security import *

pm = ['PM1','PM2','PM3']
em = ['EM1','EM2','EM3']                     
rm = ['RM1','RM2','RM3','RM4','RM5','RM6']
wm = ['WM1','WM2']                          

sub = ['PS1','PS2','PS3',  
                'RS1','RS2']        


db = pymysql.connect(host=DB.get_host(),port=DB.get_port(),user=DB.get_user(),passwd=DB.get_passwd(),db='map_data',charset=DB.get_charset())
try :
    
    with db.cursor() as cursor :
        sql = """INSERT INTO course(course_id,
        category_group_code_1,category_group_code_2,category_group_code_3,category_group_code_4)
                VALUES(%s,%s,%s,%s,%s)
            """
        num = 1
        valList = []
        for i in pm : 
            valList.append(('A_'+str(num),i,None,None,None))
            num+=1
        for i in em : 
            valList.append(('A_'+str(num),i,None,None,None))
            num+=1
        for i in rm : 
            valList.append(('A_'+str(num),i,None,None,None))
            num+=1
        for i in wm : 
            valList.append(('A_'+str(num),i,None,None,None))
            num+=1
        for i in sub : 
            valList.append(('A_'+str(num),i,None,None,None))
            num+=1

        num = 1
        for i in pm :
            for j in em+rm+wm+sub :
                valList.append(('B_'+str(num),i,j,None,None))
                num+=1          
        for i in em :
            for j in rm+wm+sub :
                valList.append(('B_'+str(num),i,j,None,None))
                num+=1          
        for i in rm :
            for j in wm+sub :
                valList.append(('B_'+str(num),i,j,None,None))
                num+=1
        for i in wm :
            for j in sub :
                valList.append(('B_'+str(num),i,j,None,None))
                num+=1             
        for i in sub :
            for j in sub :
                valList.append(('B_'+str(num),i,j,None,None))
                num+=1    

        num = 1
        for i in pm :
            for j in em:
                for k in rm+wm+sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1
        for i in pm :
            for j in rm:
                for k in wm+sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1            
        for i in pm :
            for j in wm+sub:
                for k in sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1
        for i in em :
            for j in rm:
                for k in wm+sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1
        for i in em :
            for j in wm+sub:
                for k in sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1
        for i in rm :
            for j in wm+sub:
                for k in sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1
        for i in wm+sub :
            for j in sub:
                for k in sub:
                    valList.append(('C_'+str(num),i,j,k,None))
                    num+=1

        num = 1
        for i in pm :
            for j in em:
                for k in rm:
                    for l in wm+sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in pm :
            for j in em:
                for k in wm+sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in pm :
            for j in rm:
                for k in wm+sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in pm :
            for j in wm+sub:
                for k in sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in em :
            for j in rm:
                for k in wm+sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in em :
            for j in wm+sub:
                for k in sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in rm :
            for j in wm+sub:
                for k in sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        for i in wm+sub :
            for j in sub:
                for k in sub:
                    for l in sub:
                        valList.append(('D_'+str(num),i,j,k,l))
                        num+=1
        
        cursor.executemany(sql,valList)
        db.commit()

finally :
    db.close()