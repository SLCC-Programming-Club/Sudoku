function run() {
    if [[ $# -eq 0 ]]; then
        # Compile the program
        javac -d bin *.java
        java -cp bin App
    else
        case $1 in
            "-c" | "--cli")
                # Run the program in the CLI
                # Compile the program
                javac -d bin *.java
                java -cp bin App -c
                ;;

            "-b" | "--build")
                # Compile the program
                javac -d bin *.java
                ;;

            "-j" | "--jar")
                # Create a JAR file
                jar cfe sudoku.jar App -C bin .
                ;;

            "-h" | "--help")
                echo "Usage: run.sh [OPTION]"
                echo "Run the program"
                echo
                echo "Options:"
                echo "  -c, --cli    Run the program in the CLI"
                echo "  -b, --build  Compile the program"
                echo "  -j, --jar    Create a JAR file"
                echo "  -h, --help   Display this help message"
                ;;

            *)
                echo "Invalid argument"
                ;;
        esac
    fi
}

run $@
