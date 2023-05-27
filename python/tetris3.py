import pygame
import random

# 게임 환경 설정
pygame.init()
WIDTH, HEIGHT = 800, 600
PLAY_WIDTH, PLAY_HEIGHT = 300, 600
BLOCK_SIZE = 30
ROWS, COLS = PLAY_HEIGHT // BLOCK_SIZE, PLAY_WIDTH // BLOCK_SIZE
WINDOW_SIZE = (WIDTH, HEIGHT)
PLAY_SIZE = (PLAY_WIDTH, PLAY_HEIGHT)
GRID_SIZE = (COLS, ROWS)
TOP_LEFT_X = (WIDTH - PLAY_WIDTH) // 2
TOP_LEFT_Y = HEIGHT - PLAY_HEIGHT - 50

# 테트리스 블록 모양 정의
S = [['.....',
      '.....',
      '..00.',
      '.00..',
      '.....'],
     ['.....',
      '..0..',
      '..00.',
      '...0.',
      '.....']]

Z = [['.....',
      '.....',
      '.00..',
      '..00.',
      '.....'],
     ['.....',
      '..0..',
      '.00..',
      '.0...',
      '.....']]

I = [['..0..',
      '..0..',
      '..0..',
      '..0..',
      '.....'],
     ['.....',
      '0000.',
      '.....',
      '.....',
      '.....']]

O = [['.....',
      '.....',
      '.00..',
      '.00..',
      '.....']]

J = [['.....',
      '.0...',
      '.000.',
      '.....',
      '.....'],
     ['.....',
      '..00.',
      '..0..',
      '..0..',
      '.....'],
     ['.....',
      '.....',
      '.000.',
      '...0.',
      '.....'],
     ['.....',
      '..0..',
      '..0..',
      '.00..',
      '.....']]

L = [['.....',
      '...0.',
      '.000.',
      '.....',
      '.....'],
     ['.....',
      '..0..',
      '..0..',
      '..00.',
      '.....'],
     ['.....',
      '.....',
      '.000.',
      '.0...',
      '.....'],
     ['.....',
      '.00..',
      '..0..',
      '..0..',
      '.....']]

T = [['.....',
      '..0..',
      '.000.',
      '.....',
      '.....'],
     ['.....',
      '..0..',
      '..00.',
      '..0..',
      '.....'],
     ['.....',
      '.....',
      '.000.',
      '..0..',
      '.....'],
     ['.....',
      '..0..',
      '.00..',
      '..0..',
      '.....']]

SHAPES = [S, Z, I, O, J, L, T]
SHAPES_COLORS = [(0, 255, 0), (255, 0, 0), (0, 255, 255), (255, 255, 0), (255, 165, 0), (0, 0, 255), (128, 0, 128)]


# 테트리스 블록 클래스
class Piece(object):
    rows = 20
    cols = 10

    def __init__(self, col, row, shape):
        self.x = col
        self.y = row
        self.shape = shape
        self.color = SHAPES_COLORS[SHAPES.index(shape)]
        self.rotation = 0


# 게임 보드 초기화
def create_grid(locked_positions={}):
    grid = [[(0, 0, 0) for _ in range(COLS)] for _ in range(ROWS)]

    for row in range(ROWS):
        for col in range(COLS):
            if (col, row) in locked_positions:
                c = locked_positions[(col, row)]
                grid[row][col] = c
    return grid


# 블록 회전
def rotate_shape(shape):
    return shape[(shape.rotation + 1) % len(shape.shape)]


# 충돌 감지
def is_collision(piece, grid):
    for row in range(len(piece.shape)):
        for col in range(len(piece.shape[row])):
            if piece.shape[row][col] == '0':
                if piece.y + row >= ROWS or piece.x + col < 0 or piece.x + col >= COLS or \
                        grid[piece.y + row][piece.x + col] != (0, 0, 0):
                    return True
    return False


# 블록 그리기
def draw_piece(surface, piece):
    for row in range(len(piece.shape)):
        for col in range(len(piece.shape[row])):
            if piece.shape[row][col] == '0':
                pygame.draw.rect(surface, piece.color, (TOP_LEFT_X + piece.x * BLOCK_SIZE,
                                                        TOP_LEFT_Y + piece.y * BLOCK_SIZE,
                                                        BLOCK_SIZE, BLOCK_SIZE))
                pygame.draw.rect(surface, (128, 128, 128),
                                 (TOP_LEFT_X + piece.x * BLOCK_SIZE, TOP_LEFT_Y + piece.y * BLOCK_SIZE,
                                  BLOCK_SIZE, BLOCK_SIZE), 1)


# 게임 화면 그리기
def draw_window(surface, grid, score=0):
    surface.fill((0, 0, 0))

    pygame.font.init()
    font = pygame.font.SysFont('comicsans', 60)
    label = font.render('Tetris', 1, (255, 255, 255))
    surface.blit(label, (TOP_LEFT_X + PLAY_WIDTH / 2 - (label.get_width() / 2), 30))

    pygame.font.init()
    font = pygame.font.SysFont('comicsans', 30)
    label = font.render('Score: ' + str(score), 1, (255, 255, 255))
    surface.blit(label, (TOP_LEFT_X + PLAY_WIDTH - 150, TOP_LEFT_Y - 40))

    for row in range(ROWS):
        for col in range(COLS):
            pygame.draw.rect(surface, grid[row][col], (TOP_LEFT_X + col * BLOCK_SIZE,
                                                       TOP_LEFT_Y + row * BLOCK_SIZE,
                                                       BLOCK_SIZE, BLOCK_SIZE))
            pygame.draw.rect(surface, (128, 128, 128),
                             (TOP_LEFT_X + col * BLOCK_SIZE, TOP_LEFT_Y + row * BLOCK_SIZE,
                              BLOCK_SIZE, BLOCK_SIZE), 1)

    pygame.draw.rect(surface, (255, 0, 0), (TOP_LEFT_X, TOP_LEFT_Y, PLAY_WIDTH, PLAY_HEIGHT), 5)


# 메인 게임 루프
def main():
    locked_positions = {}
    grid = create_grid(locked_positions)
    change_piece = False
    run = True
    current_piece = Piece(5, 0, random.choice(SHAPES))
    next_piece = Piece(5, 0, random.choice(SHAPES))
    clock = pygame.time.Clock()
    fall_time = 0
    fall_speed = 0.27
    score = 0

    while run:
        grid = create_grid(locked_positions)
        fall_time += clock.get_rawtime()
        clock.tick()

        if fall_time / 1000 >= fall_speed:
            fall_time = 0
            current_piece.y += 1
            if not (is_collision(current_piece, grid)) and current_piece.y + len(current_piece.shape) <= ROWS:
                current_piece.y += 1
            else:
                current_piece.y -= 1
                change_piece = True

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False
                pygame.display.quit()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_LEFT:
                    current_piece.x -= 1
                    if not (is_collision(current_piece, grid)) and current_piece.x >= 0:
                        current_piece.x -= 1
                elif event.key == pygame.K_RIGHT:
                    current_piece.x += 1
                    if not (is_collision(current_piece, grid)) and current_piece.x + len(current_piece.shape[0]) <= COLS:
                        current_piece.x += 1
                elif event.key == pygame.K_DOWN:
                    current_piece.y += 1
                    if not (is_collision(current_piece, grid)) and current_piece.y + len(current_piece.shape) <= ROWS:
                        current_piece.y += 1
                elif event.key == pygame.K_UP:
                    current_piece.rotation = (current_piece.rotation + 1) % len(current_piece.shape)
                    current_piece.shape = rotate_shape(current_piece)

        shape_pos = convert_shape_format(current_piece)

        for i in range(len(shape_pos)):
            x, y = shape_pos[i]
            if y > -1:
                grid[y][x] = current_piece.color

        if change_piece:
            for pos in shape_pos:
                p = (pos[0], pos[1])
                locked_positions[p] = current_piece.color
            current_piece = next_piece
            next_piece = Piece(5, 0, random.choice(SHAPES))
            change_piece = False
            score += clear_rows(grid, locked_positions) * 10

        draw_window(win, grid, score)
        draw_piece(win, current_piece)
        pygame.display.update()

        if check_lost(locked_positions):
            run = False

    pygame.display.quit()


# 현재 블록의 좌표 변환
def convert_shape_format(piece):
    positions = []
    shape_format = piece.shape[piece.rotation % len(piece.shape)]

    for row in range(len(shape_format)):
        for col in range(len(shape_format[row])):
            if shape_format[row][col] == '0':
                positions.append((piece.x + col, piece.y + row))

    for i, pos in enumerate(positions):
        positions[i] = (pos[0] - 2, pos[1] - 4)

    return positions


# 행이 가득 찼는지 확인하고 지움
def clear_rows(grid, locked_positions):
    cleared_rows = []
    for row in range(len(grid)):
        if (0, 0, 0) not in grid[row]:
            cleared_rows.append(row)

    if len(cleared_rows) > 0:
        for row in cleared_rows:
            del locked_positions[row]
            for col in range(COLS):
                for y in range(row, 0, -1):
                    if (col, y - 1) in locked_positions:
                        locked_positions[(col, y)] = locked_positions.pop((col, y - 1))
                        grid[y][col] = grid[y - 1][col]
                        grid[y - 1][col] = (0, 0, 0)

    return len(cleared_rows)


# 게임 오버 여부 확인
def check_lost(locked_positions):
    for pos in locked_positions:
        x, y = pos
        if y < 1:
            return True
    return False


# 게임 실행
win = pygame.display.set_mode(WINDOW_SIZE)
pygame.display.set_caption('Tetris')
main()
