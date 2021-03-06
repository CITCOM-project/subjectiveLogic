multiplyUncertainties <- function(opinions,l){
  pd = NULL
  for(j in setdiff(opinions$Label,l)){
    if(is.null(pd))
      pd = opinions[opinions$Label==j,]$Uncertainty
    else
      pd = pd * opinions[opinions$Label==j,]$Uncertainty
  }
  return(pd)
}

multiSourceWeightedFusion <- function(opinions){
  if(nrow(opinions)==1)
    return(data.frame(Label="fused",Belief = opinions$Belief, Disbelief = opinions$Disbelief, Uncertainty=opinions$Uncertainty, Apriori = opinions$Apriori))
  sm <- 0
  for(l in opinions$Label){
    el = opinions[opinions$Label==l,]$Belief * (1-opinions[opinions$Label==l,]$Uncertainty)
    pd = multiplyUncertainties(opinions,l)
    sm = sm + (el * pd)
  }
  dsm <- 0
  
  for(l in opinions$Label){
    pd = multiplyUncertainties(opinions,l)
    dsm = dsm + pd
  }
  pd = NULL
  for(l in opinions$Label){
    if(is.null(pd))
      pd = opinions[opinions$Label==l,]$Uncertainty
    else
      pd = pd * opinions[opinions$Label==l,]$Uncertainty
  }
  
  apriori = NULL
  nominator = 0
  for(l in opinions$Label){
    nominator = nominator + (opinions[opinions$Label==l,]$Apriori * (1-opinions[opinions$Label==l,]$Uncertainty))
  }
  denominator = nrow(opinions) - sum(opinions$Uncertainty )
  apriori = nominator / denominator
  
  belief = (sm / (dsm - nrow(opinions) * pd))
  
  uncertainty = ((nrow(opinions)-sum(opinions$Uncertainty))*pd)/(dsm- nrow(opinions) * pd)
  disbelief= 1-(belief + uncertainty)
  return(data.frame(Label="fused",Belief = belief, Disbelief = disbelief, Uncertainty=uncertainty, Apriori = apriori))
}


#Subtraction - compute the difference between two subjective opinions

subtract <- function(opinionA, opinionB){
  aprioriA = 0.5
  aprioriB = 0.5
  if(!is.null(opinionA$Apriori))
    aprioriA = opinionA$Apriori
  if(!is.null(opinionB$Apriori))
    aprioriB = opinionB$Apriori
  belief = max((opinionA$Belief - opinionB$Belief),0)
  disbelief = (aprioriA * (opinionA$Disbelief + opinionB$Belief) - aprioriB * (1 + opinionB$Belief - opinionA$Belief - opinionB$Uncertainty))/(aprioriA-aprioriB)
  uncertainty = ((aprioriA*opinionA$Uncertainty)-(aprioriB*opinionB$Uncertainty))/(aprioriA-aprioriB)
  apriori = aprioriA - aprioriB
  return(data.frame(Belief = belief, Disbelief = disbelief, Uncertainty = uncertainty, Apriori = apriori))
}

#Compute the projected probability from a Subjective Opinion.

projectedProbability <- function(apriori, belief, uncertainty){
  probability  = belief + (apriori * uncertainty)
  return(probability)
}



#Computes the beta-distribution that corresponds to a given subjective opinion [@josang2016subjective,@duan2016representation].

computeBeta <- function(opinion){
  if(opinion$Disbelief == 0){
    opinion$Uncertainty = opinion$Uncertainty - 0.00001
    opinion$Disbelief = 1 - (opinion$Uncertainty + opinion$Belief)
  }
  r = ((opinion$Belief * 2) / opinion$Uncertainty) + 1
  s = ((opinion$Disbelief * 2)  / opinion$Uncertainty) + 1
  
  distribution = dbeta(x=seq(0,1,length=100),shape1=r, shape2=s)
  bDist <- data.frame(x=seq(0,1,length=100),y=distribution)
  bDist$Label=rep(opinion$Label,100)
  return(bDist)
}

#Plot the barycentric triangle for a given opinion [@josang2016subjective].

plotTriangle <- function(opinion, withLines){
  triangle <- data.frame(x=c(0,1,0.5,0),y=c(0,0,0.5,0))
  coords <- data.frame(x=c(0,0.25,1,0.75,0,0.5,0.5),y=c(0,0.25,0,0.25,0,0,0.5))
  apriori <- data.frame(x=c(0.5,opinion$Apriori),y=c(0.5,0))  
  
  aX = 0
  aY = 0
  bX = 1 * opinion$Belief
  bY = 0
  cX = 0.5 * opinion$Uncertainty
  cY = cX
  pX = aX + bX + cX
  pY = aY + bY + cY
  
  proj <- projectedProbability(opinion$Apriori, opinion$Belief, opinion$Uncertainty) 
  
  projected <- data.frame(x=c(pX,proj),y=c(pY,0))
  
  p <- ggplot(triangle,aes(x=x,y=y, size=1))+geom_path(aes(size=0.5))
  if(withLines){
    p <- p + geom_path(data=coords,size=0.25,linetype = "dashed") + geom_path(data=apriori,size=0.25,colour='red') + #geom_text(aes(size=0.8),x=opinion$Apriori,y=-.015,label="Apriori", colour='red') +  #geom_text(aes(size=0.8), x=opinion$Apriori,y=0.015,label=round(opinion$Apriori,2),colour='red') + 
      geom_path(data=projected,size=0.25, colour="blue")
    #+ geom_text(aes(size=0.8),x=proj,y=-.015,label="Projected", colour='blue') + geom_text(aes(size=0.8), x=proj,y=0.015,label=round(proj,2),colour='blue')
  }
  p <- p + geom_point(aes(size=1, x=pX,y=pY), shape=8,colour="blue")+ geom_text(aes(size=0.8),label=opinion$Label,x=0,y=0.5,colour="blue", fontface = "bold",hjust=0,vjust=1) + theme(legend.position = "none",axis.title.x=element_blank(),axis.title.y=element_blank(),axis.text.y=element_blank(),axis.text.x=element_blank()) + geom_text(aes(size=0.8),label="Disbelief",x=0,y=-0.015, fontface = "bold",hjust=0) + geom_text(aes(size=0.8),label="Belief",x=1,y=-0.015, fontface = "bold",hjust=1) + geom_text(aes(size=0.8),label="Uncertainty",x=0.5,y=0.502, fontface = "bold",hjust=0) #+ geom_text(aes(size=0.5),label=paste0("(",round(opinion$Belief,2),",",round(opinion$Disbelief,2),",",round(opinion$Uncertainty,2),")"),x=pX,y=pY+0.02, colour='blue') 
  return(p)
}


#Plot a time-line - changes in opinion as a series of arrows in a barycentric triangle.

getCoord <- function(opinion){
  aX = 0
  aY = 0
  bX = 1 * opinion$Belief
  bY = 0
  cX = 0.5 * opinion$Uncertainty
  cY = cX
  pX = aX + bX + cX
  pY = aY + bY + cY
  return(data.frame(x=pX, y=pY))
}


plotTimeTriangle <- function(opinions){
  triangle <- data.frame(x=c(0,1,0.5,0),y=c(0,0,0.5,0))
  coords <- data.frame(x=c(0,0.25,1,0.75,0,0.5,0.5),y=c(0,0.25,0,0.25,0,0,0.5))
  
  xv <- c()
  yv <- c()
  
  for(i in 1:nrow(opinions)){
    o <- getCoord(opinions[i,])
    xv <- c(xv,o$x)
    yv <- c(yv,o$y)
  }
  
  route <- data.frame(x=xv,y=yv)
  
  colours <- seq(1,nrow(opinions))
  
  p <- ggplot(triangle,aes(x=x,y=y, size=1))+geom_path(aes(size=0.5)) + geom_path(data=coords,size=0.25,linetype = "dashed")  + theme(legend.position = "none",axis.title.x=element_blank(),axis.title.y=element_blank(),axis.text.y=element_blank(),axis.text.x=element_blank()) + geom_text(aes(size=0.8),label="Disbelief",x=0,y=-0.015, fontface = "bold",hjust=0) + geom_text(aes(size=0.8),label="Belief",x=1,y=-0.015, fontface = "bold",hjust=1) + geom_text(aes(size=0.8),label="Uncertainty",x=0.5,y=0.502, fontface = "bold",hjust=0) + geom_path(data=route,aes(colour=colours), size=0.25)
  return(p)
}

computeTriangles <- function(beliefs){
  triangles <- list()
  local({
    for(i in 1:(nrow(beliefs))){
      triangles[[i]] <<- plotTriangle(beliefs[i,], FALSE)
    }
    return(triangles)
  })
}

computeDistributions <- function(beliefs){
  betas <- data.frame()
  local({
    for(i in 1:(nrow(beliefs))){
      if(nrow(betas)==0){
        betas <- computeBeta(beliefs[i,])
      }
      else{
        betas <- rbind(betas,computeBeta(beliefs[i,]))
      }
    }
    return(betas)
  })
}


#For a given set of beliefs, plot a row of barycentric triangles, coupled with a set of distributions
#If rows is 1, everything should be laid out horizontally. If it's 2, everything should be vertical.

soPlot <- function(beliefs, rows, min, max){
  betas <- computeDistributions(beliefs)
  triangles <- computeTriangles(beliefs)
  
  axisSequence <- round(seq(min,max,length.out=7),2)
  
  separateDistributions <- ggplot(betas) + geom_line(aes(x=x,y=y,colour=Label))+ ylab("Probability Mass")+
    theme(axis.text.x = element_text(size=8,angle = 315),axis.title.x = element_text(size=8),
          axis.text.y = element_text(size=8),axis.title.y = element_text(size=8),legend.text = element_text(size=8),legend.title = element_blank())+ 
    xlab("Effect size") +  scale_x_continuous(breaks=seq(0,1,length.out=7),labels=c(paste("\u2264",min),axisSequence[2:6],paste("\u2265",max)))
  
  separateTriangles <- ggarrange(plotlist=triangles, ncol=length(triangles))
  final <- NULL
  if(rows == 1){
    final <- ggarrange(separateTriangles,separateDistributions,ncol=2, nrow=1, widths=c(2,4))
  }
  else{
    final <- ggarrange(separateDistributions,separateTriangles,ncol=1, nrow=2,heights=c(2,1))
  }
  return(final)
}

#Assumes a dataframe with a "Group" column, separating out the different groups of beliefs
groupedBeliefPlot <- function(beliefs, min, max){
  local({
    lowerPlots = list()
    fused = data.frame()
    lengths <- c()
    labels <- c()
    counter <- 1
    if(is.null(beliefs$Group)){
      beliefs$Group <- rep("All", nrow(beliefs))
    }
    for(g in unique(beliefs$Group)){
      subset <- beliefs[beliefs$Group==g,]
      lowerPlots[[counter]] <- soPlot(subset,2,min,max)+theme(panel.border = element_rect(colour = "black", fill=NA, size=1))
      f <- multiSourceWeightedFusion(subset)
      lengths <- c(lengths,nrow(subset))
      labels <- c(labels,g)
      f$Label <- paste("Fused\n",g)
      if(nrow(fused)==0){
        fused <- f
      }
      else{
        fused <- rbind(fused,f)
      }
      counter = counter + 1
    }
    print(fused)
    upperPlot <- soPlot(fused,1,min,max) + border()
    lowerPlot <- ggarrange(plotlist=lowerPlots,nrow=1,ncol=length(lowerPlots),widths=lengths,labels=labels) 
    finalPlot <- ggarrange(upperPlot,lowerPlot,ncol=1,nrow=2, heights=c(1,2))
    return(finalPlot)
  })
}

