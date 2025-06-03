CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL,
    email varchar(100) NOT NULL,
    CONSTRAINT uq_users_email UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description varchar(1000) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at date NOT NULL,
    -- не забывать, чтобы было на что ссылаться, эта таблица и поле уже должно существовать!!!
    CONSTRAINT fk_requests_to_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- запроса, как и владельца, может не быть
-- если предмет привязан к владельцу - может не быть запроса. Если был создан запрос - может не быть владельца
CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(1000) NOT NULL,
    owner_id BIGINT,
    request_id BIGINT,
    availability boolean NOT NULL,
    CONSTRAINT fk_items_to_user FOREIGN KEY(owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_to_request FOREIGN KEY(request_id) REFERENCES requests(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text varchar (1000) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at date NOT NULL,
    CONSTRAINT fk_comments_to_item FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_to_user FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date date NOT NULL,
    end_date date NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status varchar(100) NOT NULL,
    CONSTRAINT fk_bookings_to_item FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_to_user FOREIGN KEY(booker_id) REFERENCES users(id) ON DELETE CASCADE
);