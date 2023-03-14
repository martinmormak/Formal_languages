#include <stdio.h>
#include "greatest.h"
#include "../parser.h"

TEST verify_with_wrong_string(){
    ASSERT_EQ(0, parseString("cxyxyxyxy"));
    ASSERT_EQ(0, parseString("dxyxyxyxyaxyxyxy"));
    ASSERT_EQ(0, parseString("aaaaaaaa"));
    ASSERT_EQ(0, parseString("xyxyxyxyxyxyxy"));
    ASSERT_EQ(0, parseString("cdbac"));
    ASSERT_EQ(0, parseString("bba"));
    PASS();
}


TEST verify_with_correct_string(){
    ASSERT_EQ(1, parseString("ca"));
    ASSERT_EQ(1, parseString("da"));
    ASSERT_EQ(1, parseString("dax"));
    ASSERT_EQ(1, parseString("daxy"));
    ASSERT_EQ(3, parseString("bcacaaacaxyxyxy"));
    ASSERT_EQ(2, parseString("adaxyacaxy"));
    ASSERT_EQ(1, parseString("baaaacaxy"));
    ASSERT_EQ(1, parseString("aaaaaaaaacaxyxyxyxyxy"));
    ASSERT_EQ(3, parseString("bdaxyaaaacxyxybacxyxyxyxycaaaaaaaaaaaadaaaaaaaaaaaxy"));
    PASS();
}

SUITE (test_parser) {
    RUN_TEST(verify_with_wrong_string);
    RUN_TEST(verify_with_correct_string);
}

GREATEST_MAIN_DEFS();

int main(int argc, char **argv) {
    GREATEST_MAIN_BEGIN();
    RUN_SUITE(test_parser);
    GREATEST_MAIN_END();
}
