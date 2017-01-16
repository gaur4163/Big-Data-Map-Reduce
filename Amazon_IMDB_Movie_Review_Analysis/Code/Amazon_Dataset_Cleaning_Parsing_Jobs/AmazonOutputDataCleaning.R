
setwd("C:/Users/inigam/Documents/Semester/Amazon-Movie-Review-Analysis-master/out")
data1 <- read.csv("mergedinput.csv", header=FALSE)
# class: data.frame
data<-data.frame(data1)
names(data)<-c("Released","imdbID","Director","Title","Actor","imdbRating","imdbVote","AmazonAvgScore","runtime","Awards","Year","Language","Country","AmazonTitle","AmazonID","Genre","Writer","Poster","OscarWinner","OscarNominated","otherAwards")
data[data == "N/A"]  <- NA

data$imdbRating<-as.numeric(as.character(data$imdbRating))
data$imdbRating[which(is.na(data$imdbRating))]<-mean(data$imdbRating, na.rm=TRUE)
data$Year <- substr(data$Year,0,4)

lm.fit=lm(data$imdbRating~.-data$Released-data$Title-data$AmazonAvgScore-data$Poster-data$AmazonTitle, data=data)
summary(lm.fit)
write.csv(data,"cleanedMergedInput.csv",row.names=FALSE)



