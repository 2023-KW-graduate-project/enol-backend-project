import requests

def whole_region(keyword, category_group_code, start_x, start_y, end_x, end_y):
    page_num = 1
    # 데이터가 담길 리스트
    all_data_list = []
    
    while(1):
        # # 카테고리 검색
        # url = 'https://dapi.kakao.com/v2/local/search/keyword.json'
        # params = {'query': keyword, 'category_group_code' : category_group_code, 'page': page_num, 
        #          'rect': f'{start_x},{start_y},{end_x},{end_y}'}
        # 키워드 검색
        url = 'https://dapi.kakao.com/v2/local/search/category.json'
        params = {'category_group_code' : category_group_code, 'page': page_num, 
                 'rect': f'{start_x},{start_y},{end_x},{end_y}'}
        headers = {"Authorization" : "KakaoAK faa6dc57c28dcc48c59e46160c2b833a"}
        resp = requests.get(url, params=params, headers=headers)
        
        print(resp.json())
        search_count = resp.json()['meta']['total_count']
        
        if search_count > 45:
            dividing_x = (start_x + end_x) / 2
            dividing_y = (start_y + end_y) / 2
            ## 4등분 중 왼쪽 아래
            all_data_list.extend(whole_region(keyword, category_group_code, start_x,start_y,dividing_x,dividing_y))
            ## 4등분 중 오른쪽 아래
            all_data_list.extend(whole_region(keyword, category_group_code, dividing_x,start_y,end_x,dividing_y))
            ## 4등분 중 왼쪽 위
            all_data_list.extend(whole_region(keyword, category_group_code, start_x,dividing_y,dividing_x,end_y))
            ## 4등분 중 오른쪽 위
            all_data_list.extend(whole_region(keyword, category_group_code, dividing_x,dividing_y,end_x,end_y))
            return all_data_list
        
        else:
            if resp.json()['meta']['is_end']:
                all_data_list.extend(resp.json()['documents'])
                return all_data_list
            # 아니면 다음 페이지로 넘어가서 데이터 저장
            else:
                page_num += 1
                all_data_list.extend(resp.json()['documents'])
    return

def overlapped_data(keyword, category_group_code, start_x, start_y, next_x, next_y, num_x, num_y):
    # 최종 데이터가 담길 리스트
    overlapped_result = []

    # 지도를 사각형으로 나누면서 데이터 받아옴
    for i in range(1,num_x+1):   ## 1,54
        end_x = start_x + next_x
        initial_start_y = start_y
        for j in range(1,num_y+1):  ## 1,31
            end_y = initial_start_y + next_y
            each_result= whole_region(keyword, category_group_code, start_x,initial_start_y, end_x,end_y)
            overlapped_result.extend(each_result)
            initial_start_y = end_y
        start_x = end_x
    
    return overlapped_result