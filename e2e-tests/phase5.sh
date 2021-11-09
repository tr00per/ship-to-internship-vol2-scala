#!/bin/bash -ueE

function catch_errors() {
  kill $(jobs -p)
 	echo "####  Program failed with errors. (Exit code is not 0)  Full console output of your program:";
	cat $FOUT
  exit 1
}

function show_link() {
    echo -e "\n#############################################\n"
    echo "Congratulations, you successfully pass phase 4"
    echo -e "\n#############################################\n"
}

trap catch_errors ERR;
[[ ${DEBUG:-} ]] && set -x

MODE=${MODE:-check}

if [[ $OSTYPE == darwin* ]]; then
    echo "Ensure you have coreutils installed"
    alias readlink=greadlink
fi

MYDIR="$(readlink -f $(dirname $0))"

echo "Building..."
sbt clean assembly

counter=0

for suite in "$MYDIR/test-vectors/phase5/"**; do
    cat "$suite/title.txt"
    echo -e "\n\nRunning Web...\n"
    java -jar target/scala-2.13/idemiomat.jar &
    sleep 5
    for FIN in "$suite"/*_in; do
        BASE=$(basename "$FIN")
        echo -e "####  Starting test $BASE ####"

        FTEST=$(dirname "$FIN")/${BASE/%_in/_expected}

        FOUT=$(dirname "$FIN")/${BASE/%_in/_actual}

        if [[ $MODE == generate ]]; then
            FOUT="$FTEST"
        fi

        if [[ $MODE == check ]]; then
            echo -e "\nINPUT ($(wc -l $FIN | cut -d' ' -f1) lines)"
            cat $FIN
        fi

        PROTOCOL=$(sed -n '1p' $FIN)
        ADDRESS=$(sed -n '2p' $FIN)
        LINE_NUMBER=$(awk 'END { print NR }' $FIN)
        REQUEST="$(sed -n '3,1000p' $FIN)"

        curl -s --connect-timeout 3 --max-time 30 -X $PROTOCOL -H 'Content-Type: application/json' --data "$REQUEST" "http://localhost:8080$ADDRESS"
        curl -o $FOUT -s --connect-timeout 3 --max-time 30 -X GET http://localhost:8080/idemiomat/size

        if [[ $MODE == check ]]; then
            echo -e "\nLEFT: expected, RIGHT: actual"
            diff -yB "$FTEST" "$FOUT"
            echo -e "####  Test $BASE passed  ####"
            counter=$((counter+1))
        fi
    done
    kill $(jobs -p)
done

if [[ $counter == 3 ]]; then show_link
fi

