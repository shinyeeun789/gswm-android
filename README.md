# GSWM
Android Studio 4.1 환경에서 Java를 사용하여 개발한 앱

## 💻 프로젝트 소개
최근 들어 공부하는 모습을 찍어 SNS에 인증하거나 Youtube에서 공부 방송을 진행하는 등 공부 습관에 대한 관심도 나날이 높아지고 있습니다. <br/>
본 시스템은 사용자가 세운 목표를 달성함으로써 얻는 성취감으로 꾸준히 공부에 흥미를 느낄 수 있도록 도움을 주고자 개발하였습니다. <br/>
개발된 시스템을 통해 사용자는 쉽고 빠르게 스터디 플랜을 세워 이를 바탕으로 모든 목표를 달성할 수 있게 될 것이라 기대됩니다. <br/>

### 🕰 개발 기간
2021.03.02 - 2021.06.30

### 📚개발 환경
![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?&style=for-the-badge&logo=Android%20Studio&logoColor=white)
![Room](https://img.shields.io/badge/Room-1976D2.svg?&style=for-the-badge&logo=Java&logoColor=white)

## 📱 주요 기능
### 1. 목표 및 디데이 추가
#### (1) 목표 추가
- 할 일과 하루 목표 분량 선택
- 시작 시간 선택
- 반복되는 목표 추가
#### (2) 디데이 추가
- 디데이 명과 날짜 선택
#### 📷 View
<table align="center">
  <tbody>
    <tr>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234168791-ee407d05-3f06-4529-aaeb-3308b24024cc.png" width="200" alt=""/><br/>
        <sub><b> PIC1 : 목표/디데이 추가 Dialog </b></sub><br/></td>
      </td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234161523-51a9b5e9-de40-4272-9ce5-f0172294b9fb.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 목표 추가</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234169554-e1a779ba-732b-450d-842d-e4f73fd06385.png" width="200" alt=""/><br />
        <sub><b>PIC3 : 반복 목표 추가</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234163527-ff565992-6901-469e-b031-a07b7267dbba.png" width="200" alt=""/><br />
        <sub><b>PIC4 : 디데이 추가</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 2. 캘린더 및 To-Do List
#### (1) 캘린더
- MaterialCalendarView 라이브러리 사용
- 목표는 보라색으로, 디데이는 노란색으로 표시
#### (2) To-Do List
- 목표와 디데이를 RecyclerView에 동적으로 표시
- 목표 선택 시 목표 달성 여부를 변경할 수 있는 Dialog 출력
- Dialog의 ‘미달성’ 버튼 선택 시 목표 Table의 목표 달성 여부 필드가 X로 수정되고, ‘달성’ 버튼 선택 시 O로 수정
- Dialog의 '미룸' 버튼 선택 시 [<a href="https://github.com/shinyeeun789/GSWM/edit/master/README.md#%EB%AA%A9%ED%91%9C-%EB%AF%B8%EB%A3%A8%EA%B8%B0">목표 미루기</a>] 기능으로 이동
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234164388-02b55599-4b59-4693-bb65-09757b3bb803.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 캘린더</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234164511-c77548c4-d90d-497c-847f-e106ffbf3c9d.png" width="200" alt=""/><br />
        <sub><b>PIC2 : To-Do List</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 목표 미루기
- 목표 Table에서 기존 목표의 달성 여부를 ⭢로, 미달성 분량을 입력된 값으로 수정
- 사용자가 선택한 날짜에 목표를 새로 추가
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234169821-5eaf0ad2-4b90-4d45-a6e5-e0c885b70626.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 목표 미루기</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234169855-2294f447-6a8f-41dd-831d-fc88bb526bff.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 목표 미룬 후 To-Do List</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 홈 화면
- 상단에는 오늘 날짜와 총 공부 시간 출력
- 하단에는 오늘의 목표가 RecyclerView에 동적 목록으로 출력
  - 목표가 없는 경우 기본값 '공부시간 측정' 출력
- 원하는 목표 선택 시 [<a href="https://github.com/shinyeeun789/GSWM/edit/master/README.md#스톱워치">스톱워치</a>] 기능으로 이동
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234170519-fccea689-0a2e-4ef9-abe3-d5e8367af35d.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 초기 홈</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234170594-696ea9f0-00a4-486a-a039-93d0430504c4.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 공부시간 측정 후 홈</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 스톱워치
- Chronometer를 사용해 공부 시간 측정
  - 앱이 잠자기 및 앱 대기 모드에 빠지더라도 측정이 중지되지 않도록 설정
- Foreground Service
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234171036-9d49e987-df3b-4a2b-8389-0ce1b3f2fadf.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 스톱워치</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234171126-64a4bdba-c6e4-469f-964d-3ca42bb14e80.png" width="200" alt=""/><br />
        <sub><b>PIC2 : Foreground Service 실행</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 통계
- 일간 통계 / 주간 통계 / 월간 통계
- 캘린더에서 선택된 날짜에 해당하는 총 공부시간, 최대 집중 시간, 목표 달성률, 목표별 달성률 출력
- MPAndroidChart로 목표별 달성률 통계 도식화
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234171718-7b8f83ac-1d05-4011-8cb4-7d6ba7fa9c52.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 일간 통계</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234172483-b5248cc9-d7a0-4d5b-b1bf-d84246c0395f.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 주간 통계</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234172551-44230131-e3ab-4e0b-b191-41795ebcf719.png" width="200" alt=""/><br />
        <sub><b>PIC3 : 월간 통계</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 장소
- Google Map API 사용
- Googple Places API 사용
- fusedLocationProviderClient를 사용해 사용자의 위치 정보 획득 후 지도 위치 변경
- 사용자가 위치 장소의 500미터 내에 있는 카페, 도서관 정보를 마커로 GoogleMap에 표시
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234172952-5deafdea-f45f-4276-9b16-08b17215d8a3.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 현재 내 위치</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234172965-29d8fffe-6a60-4b8c-a0e6-ee611295e65f.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 근처 카페 찾기</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234172978-973f6075-b6ae-4741-aeff-6561f9d89a58.png" width="200" alt=""/><br />
        <sub><b>PIC3 : 근처 도서관 찾기</b></sub><br/></td>
    </tr>
  </tbody>
</table>

### 보상
- 화면 상단에 달성률이 100%인 목표의 개수 출력 (3개월에 한 번씩 초기화)
- 받을 수 있는 보상의 배지 개수 옆에 선물 표시 출력
#### 📷 View
<table align="center">
  <tbody>
    <tr>
       <td align="center">
         <img src="https://user-images.githubusercontent.com/70800414/234173387-d50e067a-b67a-41f2-b591-601a1d93c4c1.png" width="200" alt=""/><br />
         <sub><b>PIC1 : 보상 추가</b></sub><br/></td>
      <td align="center">
        <img src="https://user-images.githubusercontent.com/70800414/234173407-0d9bc6fb-dc2f-4008-9c70-378f0fdb0e01.png" width="200" alt=""/><br />
        <sub><b>PIC2 : 보상</b></sub><br/></td>
    </tr>
  </tbody>
</table>
