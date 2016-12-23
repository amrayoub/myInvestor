package com.myinvestor.cluster

import akka.actor.{Actor, ActorLogging}

/**
  * Creates the [[akka.cluster.Cluster]] and does any cluster lifecycle handling
  * aside from join and leave so that the implementing applications can
  * customize when this is done.
  *
  * Implemented by [[ClusterAwareNodeGuardian]].
  */
abstract class ClusterAware extends Actor with ActorLogging {
}

/**
  * A 'NodeGuardian' is the root of each MyInvestor deployed application, where
  * any special application logic is handled in its implementers, but the cluster
  * work and node lifecycle and supervision events are handled here.
  *
  * It extends [[ClusterAware]] which handles creation of the [[akka.cluster.Cluster]],
  * but does the cluster.join and cluster.leave itself.
  *
  * 'NodeGuardianLike' also handles graceful shutdown of the node and all child actors. *
  * */
abstract class ClusterAwareNodeGuardian extends ClusterAware {

}
