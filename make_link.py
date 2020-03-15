import os
import argparse


if __name__ == "__main__":
    if not os.name == "nt":
        print("Sorry this utility is windows only.")
    else:
        parser = argparse.ArgumentParser(description='Utility to create sym-links needed for mods.')
        parser.add_argument("mod_id")
        args = parser.parse_args()
        if not os.path.exists(args.mod_id):
            os.mkdir(args.mod_id)
        os.chdir(args.mod_id)
        os.system("mklink settings.gradle \"..\\base\\settings.gradle\"")
        os.system("mklink gradlew \"..\\base\\gradlew\"")
        os.system("mklink gradlew.bat \"..\\base\\gradlew.bat\"")
        os.system("mklink /D gradle \"..\\base\\gradle\"")
