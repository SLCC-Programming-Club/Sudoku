run() {
    javac *.java
    java App $1
    rm *.class
}

if($# -eq 1) then
    FLAG=$1
fi

run $FLAG