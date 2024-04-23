from sqlalchemy import create_engine

from database.entities import Base


def main():
    engine = create_engine("sqlite:///data.sqlite", echo=True)
    Base.metadata.create_all(engine)


if __name__ == "__main__":
    main()
