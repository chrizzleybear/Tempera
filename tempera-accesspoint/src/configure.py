import time
from getpass import getpass
from pathlib import Path
from typing import Literal
from uuid import UUID

import yaml
from tenacity import retry

welcome_art = """
#######################################################
#                                                     #
#   ____ ___  _   _ _____ ___ ____ _   _ ____  _____  #
#  / ___/ _ \| \ | |  ___|_ _/ ___| | | |  _ \| ____| #
# | |  | | | |  \| | |_   | | |  _| | | | |_) |  _|   #
# | |__| |_| | |\  |  _|  | | |_| | |_| |  _ <| |___  #
#  \____\___/|_| \_|_|   |___\____|\___/|_| \_\_____| #
# |_   _| ____|  \/  |  _ \| ____|  _ \    / \        #
#   | | |  _| | |\/| | |_) |  _| | |_) |  / _ \       #
#   | | | |___| |  | |  __/| |___|  _ <  / ___ \      #
#   |_| |_____|_|  |_|_|   |_____|_| \_\/_/   \_\     #
#                                                     #
#######################################################
"""


@retry()
def _prompt(
    message: str, parse: Literal["UUID", "float", "str"] = "str"
) -> float | str:
    parameter = input(message)

    if not parameter or parameter == "":
        print("---\n❌ No input provided ❌\n➡️  Please provide an input.\n---")
        raise ValueError

    match parse:
        case "UUID":
            try:
                # Ensure that the input is a valid UUID but return the original string for API validation.
                # (The UUID __repr__ isn't the same as the input when writing it to file,
                # the python object represents it as an int or hex which can cause ambiguity
                # when comparing it to the representation of the web server.)
                UUID(parameter)
                return parameter
            except ValueError:
                print(
                    "---\n❌ Provided UUID is not valid. ❌\n➡️  Please try again.\n---"
                )
                raise ValueError
        case "float":
            try:
                return float(parameter)
            except ValueError:
                print("---\n❌ Expected a number. ❌\n➡️  Please try again.\n---")
                raise ValueError
        case _:
            return parameter


@retry()
def _prompt_password() -> str:
    parameter = getpass()

    if parameter == "":
        print(
            "---\n❌ The password field cannot be empty. ❌\n➡️  Please try again.\n---"
        )
        raise ValueError

    return parameter


def main():
    print(welcome_art)

    config_file = Path(__file__).parent.resolve() / "conf.yaml"
    config = dict()

    with open(config_file, "w") as cf:
        config["access_point_id"] = _prompt(
            "Set the ID of this access point >> ", "UUID"
        )

        config["webserver_address"] = "http://" + _prompt(
            "Set the IP address and port of the web server in the following format: <IP-address>:<port>\n"
            "http://",
        )

        config["user_name"] = _prompt(
            "Set the user name for the web server api authentication >> "
        )
        config["password"] = _prompt_password()

        config["sending_interval"] = _prompt(
            "Set a data transfer interval in seconds between access point and web server >> ",
            "float",
        )

        yaml.dump(config, cf)

    print("\nSetup done! ✨🚀✨\nGood bye.\n\n")

    time.sleep(0.5)


if __name__ == "__main__":
    main()
