# GSWM
Android Studio 4.1 환경에서 Java를 사용하여 개발한 앱

## 💻 프로젝트 소개
최근 들어 공부하는 모습을 찍어 SNS에 인증하거나 Youtube에서 공부 방송을 진행하는 등 공부 습관에 대한 관심도가 나날이 높아지고 있다.
본 시스템은 사용자가 세운 목표를 달성함으로써 얻는 성취감으로 꾸준히 공부에 흥미를 느낄 수 있도록 도움을 주고자 개발하였다.

### 🕰 개발 기간
2021.03.02 - 2021.06.30

### 📚개발 환경
![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?&style=for-the-badge&logo=Android%20Studio&logoColor=white)
![Room](https://img.shields.io/badge/Room-1976D2.svg?&style=for-the-badge&logo=Java&logoColor=white)

## 📱 주요 기능
### 목표 및 디데이 추가
#### (1) 목표 추가
- 할 일과 하루 목표 분량 선택
- 시작 시간 선택
- 반복되는 목표 추가
- Room의 목표 Table에 저장
#### (2) 디데이 추가
- 디데이 명과 날짜 선택
- Room의 디데이 Table에 저장
#### 📷 View
<details>
<summary> [pic1] 목표 추가 </summary>
  <div markdown="1">
    <img src="https://user-images.githubusercontent.com/70800414/234161523-51a9b5e9-de40-4272-9ce5-f0172294b9fb.png" height="350"/>
  </div>
</details>
<details>
<summary>  [pic2] 디데이 추가 </summary>
  <div markdown="1">
    <img src="https://user-images.githubusercontent.com/70800414/234163527-ff565992-6901-469e-b031-a07b7267dbba.png" height="350"/>
  </div>
</details><br/>

### 캘린더 및 To-Do List
#### (1) 캘린더
- MaterialCalendarView 라이브러리 사용
- 목표는 보라색으로, 디데이는 노란색으로 표시
#### (2) To-Do List
- 캘린더에서 선택한 날짜의 목표와 디데이가 RecyclerView에 동적으로 표시
- RecyclerView의 목표 선택 시 목표 달성 여부를 변경할 수 있는 Dialog 출력
- Dialog의 ‘미달성’ 버튼 선택 시 목표 데이터베이스의 목표 달성 여부 필드가 X로 수정되고, ‘달성’ 버튼 선택 시 O로 수정
- Dialog의 '미룸' 버튼 선택 시 [목표 미루기] 기능으로 이동
#### 📷 View
<details>
<summary>  [pic1] 캘린더 </summary>
  <div markdown="1">
    <img src="https://user-images.githubusercontent.com/70800414/234164388-02b55599-4b59-4693-bb65-09757b3bb803.png" height="350"/>
  </div>
</details>
<details>
<summary>  [pic2] To-Do List </summary>
  <div markdown="1">
    <img src="https://user-images.githubusercontent.com/70800414/234164511-c77548c4-d90d-497c-847f-e106ffbf3c9d.png" height="350"/>
  </div>
</details><br/>

### 목표 미루기
- 목표 Table에서 기존 목표의 달성 여부를 ⭢로, 미달성 분량을 입력된 값으로 수정
- 사용자가 선택한 날짜에 목표를 새로 추가
#### 📷 View
<details>
<summary>  [pic1] 목표 미루기 </summary>
  <div markdown="1">
    <img src="https://user-images.githubusercontent.com/70800414/234164760-8879328d-2f15-4feb-8e22-833a80473153.png" height="350"/>
  </div>
</details><br/>

### 홈 화면
- 상단에는 오늘 날짜와 총 공부 시간 출력
- 하단에는 오늘의 목표가 RecyclerView에 동적 목록으로 출력
  - 목표가 없는 경우 기본값 '공부시간 측정' 출력
- 원하는 목표 선택 시 [스톱워치] 기능으로 이동

5. 스톱워치
6. 통계
7. 장소
8. 보상
