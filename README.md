#  🎨 Arti - 추천을 통한 나만의 미술관 🎨
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br>
Arti 서버 링크: https://j11d106.p.ssafy.io/
Arti 앱 다운로드 링크:

<br><br><br>

## 서비스 개요
'Arti'는 사용자들이 나만의 미술관을 즐기고, 그 안에서 다양한 작품과 음악을 경험할 수 있는 앱입니다.

사용자는 미술관을 구독하거나 자신만의 포트폴리오를 관리하며, AI 작품과 실시간으로 생성된 음악을 통해 몰입감을 느낄 수 있습니다.

또한, 검색을 통해 작품 추천을 받을 수 있습니다.

<br><br>

## 주요 기능

- 회원가입 및 콜드스타트 해결
    - 사용자가 처음 앱을 사용하면, 추천을 위한 데이터를 확보하기 위해 기본 정보를 수집하고 맞춤형 작품을 제공합니다. 이를 통해 콜드스타트 문제를 해결하고, 빠르게 사용자가 흥미를 느낄 수 있도록 지원합니다.

- 인스타그램 연동
    - 사용자의 본인 인스타 계정을 연동해서 자신이 업로드했던 이미지를 가져올 수 있습니다.

- 미술관 페이지(음악 생성, AR)
    - 사용자가 소유한 미술관 페이지에서는 실시간으로 생성된 음악을 배경으로 감상할 수 있으며, 증강현실(AR)을 통해 작품을 더욱 생생하게 경험할 수 있습니다.

- 검색 및 추천 기능
    - 사용자는 검색 페이지에서 화가, 장르, 미술관을 랜덤으로 추천받을 수 있습니다.
    - 추천 시스템을 통해 사용자의 최근 관심사에 맞는 작품을 추천받을 수 있습니다.

- 구독한 미술관 저장
    - 사용자는 자신이 관심 있는 미술관을 구독하고 저장할 수 있습니다. 구독한 미술관을 통해 새로운 작품이나 전시 정보를 빠르게 확인할 수 있습니다.

- 포트폴리오 관리
    - 포트폴리오 기능을 통해 사용자는 자신이 좋아하는 장르의 비율을 분석하고, 관심 있는 화가를 조회할 수 있습니다. 이를 통해 사용자들은 자신의 취향을 더 명확히 알 수 있습니다.

<br><br>

## 프로젝트 정보
- 프로젝트명 :  
  SSAFY 11기 2학기 특화 프로젝트 - Arti

- 기간 :  
  2024/08/18 ~ 2024/10/11

- 팀원 :  
  육각형 6조(D106)의 팀원들을 소개합니다!

  | 이름   | 역할          | 개발 내용                   |
    | ------ | ------------- | --------------------------- |
  | 황병현 | 팀장<br/>Data | - a<br/>- b<br/>- c<br/>- d |
  | 김헌우 | Backend       | - a<br/>- b<br/>- c<br/>- d |
  | 김주현 | Backend       | - a<br/>- b<br/>- c<br/>- d |
  | 박민희 | Backend       | - a<br/>- b<br/>- c<br/>- d |
  | 김민조 | Android       | - a<br/>- b<br/>- c<br/>- d |
  | 문동진 | Android       | - a<br/>- b<br/>- c<br/>- d |

<br><br>

## 주요 기술 및 개발 환경

### 아키텍처 구성도
![Web App Reference Architecture (9)](/uploads/6e615bdc557699487fbcc2b43214a6ed/아키텍쳐.png)

### **Android**

- **Build Tools:**
    - Gradle
    - Hilt
    - Kotlin KAPT
- **Libraries:**
    - Core KTX
    - AppCompat
    - Material Components
    - ConstraintLayout
- **Dependency Injection:**
    - Hilt
- **Networking:**
    - Retrofit
    - OkHttp Logging Interceptor
- **Coroutines:**
    - Kotlin Coroutines Core
    - Kotlin Coroutines Android
- **ViewModel:**
    - AndroidX Lifecycle ViewModel KTX
- **Navigation:**
    - AndroidX Navigation Fragment KTX
    - AndroidX Navigation UI KTX
- **Logging:**
    - Timber
- **AR (Augmented Reality):**
    - ARSceneView

### Backend

- **Java 17**

- **Spring Boot**

    - Spring Data JPA

    - Spring Data Redis

    - Spring Mail

    - Spring Security

    - Spring Validation

    - Spring Web

    - Spring WebFlux

- **AWS S3**

- **JWT (JSON Web Token)**

- **QueryDSL**

- **OpenCSV**

- **Logstash**

- **Lombok**

- **H2 Database**

- **MySQL**

- **사용한 외부 API**

    - Google HTTP Client

    - SpringDoc OpenAPI



### **AI**

- **Deep Learning Frameworks:**
    - TensorFlow
    - PyTorch
- **NLP (Natural Language Processing):**
    - HuggingFace Transformers
    - Tokenizers
- **Machine Learning Utilities:**
    - Faiss
    - Opt-einsum
- **Speech & Text Processing:**
    - Google Cloud Text-to-Speech (Google TTS Cloud AI)
    - AWS Transcribe (AWS Transcribe)
    - TensorBoard
    - Web Speech API
- **Data Processing:**
    - Pandas
    - Numpy
    - Scipy
- **GPU Support:**
    - CUDA
- **Model Deployment:**
    - Uvicorn for ASGI server
    - FastAPI for API development
- **Optimization and Performance:**
    - Cython
    - Zstandard
- **Logging and Monitoring:**
    - Loguru
    - Rich

### CI/CD

- AWS EC2
- Docker
- Jenkins

<br><br>

## 협업 툴

- GitLab - 코드 버전 관리 및 MR과 리뷰
- Jira - 매주 목표량 설정, 프로젝트 진행도 확인
- Notion - 회의록 작성, 기술 레퍼런스 공유, 프로젝트 산출물 관리
- Figma - 목업, 와이어프레임, 디자인 공유
- MatterMost - 자료 및 api 현황과 요청사항 공유

<br><br>

## 포팅 메뉴얼(파일 필요)

[포팅 메뉴얼 링크!](넣어야함.)

<br><br>


## 프로젝트 산출물



### UI 디자인

<img width="900" alt="image" src="/uploads/4437023cf46c730b991e42c17072d585/arti_피그마.png">
<br><br>

### ERD

<img width="900" alt="image" src="/uploads/71c9f98142a1b2cc4af1c2c3791839d0/Arti_ERD.png">
<br><br>

## UCC 및 시연영상(gif 업로드 필요)

- UCC - 유튜브 링크
- 시연영상 - 유튜브 링크 혹은 gif
  <br><br>

## 주요 서비스 화면(gif 추후 기입 예정)

### 1. 회원가입
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 2. 미술관 페이지
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 3. 검색 및 추천
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 4. AI 음악 생성
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 5. AI 작품 생성
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 6. 구독
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>

### 7. 나만의 포트폴리오
<img width="350" alt="image" src="/uploads/d38d43c1c034b140a5fe333b4ec41c19/Arti_로고.png">
<br><br><br>