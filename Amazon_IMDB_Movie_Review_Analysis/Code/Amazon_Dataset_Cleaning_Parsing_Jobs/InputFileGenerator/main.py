#!/usr/bin/env python

out = open('input.csv', 'w')
out.write("ProductId,UserId,Score" + '\n')
f = open('amazonreview_short.txt', 'r').readlines()

for line in f:
    print "Parsing " + line
    if "product/productId" in line:
        out.write('\n')
    if "productId:" in line:
        line = line.strip()
        label, val = line.split(":")
        if label == "product/productId":
            print val + ","
            out.write(val + ",")
    elif "review/userId" in line:
            line = line.strip()
            label, val = line.split(":")
            if label == "review/userId":
                print val + ","
                out.write(val + ",")
    elif "review/score" in line:
        line = line.strip()
        label, val = line.split(":")
        if label == "review/score":
            print val + ","
            out.write(val + ",")
    else:
        print "No val in " + line
