package com.ssachtleben.sbt.assets.filter

import sbt._
import sbt.Keys._
import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.pipeline.Pipeline

object Import {

  val filter = TaskKey[Pipeline.Stage]("assets-filter", "Filter intermediate files on the asset pipeline.")

}

object AssetsFilterPlugin extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import autoImport._

  override def projectSettings = Seq(
    filter := runFilter.value,
    includeFilter in filter := NothingFilter,
    excludeFilter in filter := NothingFilter
  )

  private def runFilter: Def.Initialize[Task[Pipeline.Stage]] = Def.task {
    mappings =>
      val include = (includeFilter in filter).value
      val exclude = (excludeFilter in filter).value
      mappings.filter(f => exclude.accept(f._1) || !include.accept(f._1) || f._2.startsWith("lib/"))
  }

}