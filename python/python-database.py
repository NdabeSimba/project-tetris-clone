import mysql.connector

# 데이터베이스 연결 설정
connection = mysql.connector.connect(
  host="localhost",
  user="yourusername",
  password="yourpassword",
  database="yourdatabase"
)

# 게임 종료 시 점수 저장
def save_score(player_name, score):
    cursor = connection.cursor()
    sql = "INSERT INTO scores (player_name, score) VALUES (%s, %s)"
    values = (player_name, score)
    cursor.execute(sql, values)
    connection.commit()
    print("Score saved successfully!")

# 데이터베이스 연결 종료
def close_connection():
    connection.close()

# 사용 예시
player_name = "John"
score = 1000
save_score(player_name, score)
close_connection()
