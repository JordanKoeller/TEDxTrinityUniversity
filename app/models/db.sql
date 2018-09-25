
CREATE TABLE event (
    id int NOT NULL,
    title varchar(1000) NOT NULL,
    subtitle varchar(1000),
    description text NOT NULL,
    venue varchar(1000) NOT NULL,
    date date NOT NULL,
    time time NOT NULL,
    max_seats int NOT NULL,
    taken_seats int NOT NULL,
    registration_link varchar(1000) NOT NULL,
    media_link varchar(1000),
    PRIMARY KEY (event_id)
);

CREATE TABLE posters (
    id int NOT NULL,
    name varchar(100) NOT NULL,
    email varchar(40) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE newsletter_post (
    id int NOT NULL,
    date date NOT NULL,
    description text NOT NULL,
    user_id int NOT NULL,
    title varchar(1000) NOT NULL,
    subtitle varchar(1000) NOT NULL,
    abstract varchar(1000) NOT NULL,
    media_link varchar(1000)    
);

CREATE TABLE speakers (
    speaker_id int NOT NULL,
    event_id int NOT NULL,
    name varchar(1000) NOT NULL,
    bio text NOT NULL,
    media varchar(1000) NOT NULL,
    PRIMARY KEY (speaker_id)
);

CREATE TABLE team_member (
    id int NOT NULL,
    is_active int NOT NULL,
    name varchar(1000) NOT NULL,
    position varchar(1000) NOT NULL,
    major varchar(1000) NOT NULL,
    grad_class int NOT NULL,
    biography text NOT NULL,
    email varchar(1000) NOT NULL,
    media_url varchar(1000) NOT NULL
);


