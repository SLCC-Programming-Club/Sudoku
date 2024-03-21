run() {
    javac *.java
    java App $1
    rm *.class
    rm gui/*.class
    rm gui/backend/*.class
}

if($# -eq 1) then
    FLAG=$1
fi

run $FLAG