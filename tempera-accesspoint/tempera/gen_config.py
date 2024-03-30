from pathlib import Path

import yaml

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


def main():
    print(welcome_art)

    config = {}
    config_file = Path(__file__).parent.parent / "conf.yaml"

    with open(config_file, "w") as cf:
        config["sending_interval"] = prompt(
            # TODO: add info about interval duration unit to prompt
            "Please set a data transfer interval\n"
            "between access point and web server >> ",
            parse_float=True,
        )
        config["webserver_address"] = prompt(
            "Please provide the IP-address of the web server >> "
        )
        config["accesspoint_id"] = prompt("Please set the ID of this access point >> ")
        yaml.dump(config, cf)

    print("\nSetup done! ✨🚀✨\nGood bye. ")
    import time

    time.sleep(0.5)


def prompt(message: str, parse_float: bool = False) -> str | float:
    param = None
    while not param:
        param = input(message)
        if not param:
            print("---\n❌ No input provided ❌\n➡️  Please provide an input.\n---")
        elif parse_float:
            try:
                return float(param)
            except ValueError:
                print(
                    "---\n❌ The sending interval must be a number (int or float) ❌\n➡️  Please try again.\n---"
                )
                param = None  # or the loop wouldn't continue
    return param


if __name__ == "__main__":
    main()
