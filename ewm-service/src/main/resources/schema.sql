

CREATE TABLE IF NOT EXISTS categories (
id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name VARCHAR(50) UNIQUE,
CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name  VARCHAR(250) NOT NULL unique ,
email VARCHAR(254) NOT NULL UNIQUE,
CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
annotation VARCHAR(2000) NOT NULL,
category_id BIGINT  NOT NULL,
created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
description VARCHAR(7000)  NOT NULL,
event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
initiator_id BIGINT NOT NULL,
lat FLOAT NOT NULL,
lon FLOAT NOT NULL,
paid BOOLEAN NOT NULL,
participant_limit BIGINT NOT NULL,
published_on TIMESTAMP WITHOUT TIME ZONE,
request_moderation BOOLEAN  NOT NULL,
state  VARCHAR(15) NOT NULL,
title VARCHAR(120) NOT NULL,
CONSTRAINT pk_events PRIMARY KEY (id),
CONSTRAINT fk_category_id_events FOREIGN KEY (category_id) REFERENCES categories (id),
CONSTRAINT fk_initiator_id_events FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
text VARCHAR(2000),
creator_id BIGINT NOT NULL,
event_id BIGINT NOT NULL,
create_Time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
update_Time TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT pk_comm PRIMARY KEY (id),
CONSTRAINT fk_comm_to_creator FOREIGN KEY (creator_id) REFERENCES users (id),
CONSTRAINT fk_comm_to_event FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS request (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
event_id BIGINT NOT NULL,
requester_id BIGINT NOT NULL,
status VARCHAR(50)  NOT NULL,
CONSTRAINT pk_request PRIMARY KEY (id),
CONSTRAINT fk_requester_id_events FOREIGN KEY (requester_id) REFERENCES users (id),
CONSTRAINT fk_event_id_events FOREIGN KEY (event_id) REFERENCES events (id),
UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilation (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
pinned BOOLEAN,
title VARCHAR(512) NOT NULL,
CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_and_event (
compilation_id BIGINT NOT NULL,
event_id BIGINT NOT NULL,
CONSTRAINT pk_comp_and_event PRIMARY KEY (compilation_id, event_id),
CONSTRAINT fk_comp FOREIGN KEY (compilation_id) REFERENCES compilation (id),
CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id)
);