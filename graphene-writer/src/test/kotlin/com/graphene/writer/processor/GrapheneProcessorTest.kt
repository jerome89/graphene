package com.graphene.writer.processor

import com.graphene.common.utils.DateTimeUtils
import com.graphene.writer.blacklist.BlacklistService
import com.graphene.writer.input.GrapheneMetric
import com.graphene.writer.input.Source
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import java.util.*

internal class GrapheneProcessorTest {

  private lateinit var grapheneProcessor: GrapheneProcessor

  private var applicationEventPublisher: ApplicationEventPublisher = mockk()
  private var blacklistService: BlacklistService = mockk()

  @BeforeEach
  internal fun setUp() {
    applicationEventPublisher.also {
      every { it.publishEvent(any()) } just Runs
    }

    blacklistService.also {
      every { blacklistService.isBlackListed(any()) } answers { false }
    }

    grapheneProcessor = GrapheneProcessor(blacklistService, applicationEventPublisher, mockk())
  }

  @Test
  internal fun `should drop if metric size more than 512 bytes`() {
    // when
    grapheneProcessor.process(GrapheneMetric(
      source = Source.PROMETHEUS,
      id = "docker_container_mem_active_anon",
      meta = mutableMapOf(),
      tags = TreeMap<String, String>(
        mapOf(
          "annotation_io_kubernetes_container_hash" to "24472065",
          "annotation_io_kubernetes_container_ports" to "[{\"containerPort\":8080,\"protocol\":\"TCP\"}]",
          "annotation_io_kubernetes_container_restartCount" to "0",
          "annotation_io_kubernetes_container_terminationMessagePath" to "/dev/termination-log",
          "annotation_io_kubernetes_container_terminationMessagePolicy" to "File",
          "annotation_io_kubernetes_pod_terminationGracePeriod" to "30",
          "cluster" to "comdev-p02",
          "container_image" to "ecr.baemin.in/backoffice-auth/auth-admin@sha256",
          "container_name" to "k8s_auth-admin_auth-admin-5749cfdcd4-v87nb_backoffice-auth-45-comdev-1403-servi-l4v98p_01650f54-fb3c-441f-9d58-02c4c89ffda4_0",
          "container_status" to "running",
          "container_version" to "8ce2bc4c7f8705f1cb1905abfd61957dc083216e141ed10edbaaf8a041b5cc01",
          "engine_host" to "ip-127-0-0-1",
          "host" to "ip-127-0-0-1.ap-northeast-2.compute.internal",
          "io_kubernetes_container_logpath" to "/var/log/pods/backoffice-auth-45-comdev-1403-servi-l4v98p_auth-admin-5749cfdcd4-v87nb_01650f54-fb3c-441f-9d58-02c4c89ffda4/auth-admin/0.log",
          "io_kubernetes_container_name" to "container",
          "io_kubernetes_pod_name" to "auth-admin-5749cfdcd4-v87nb",
          "io_kubernetes_pod_namespace" to "backoffice-auth-45-comdev-1403-servi-l4v98p",
          "io_kubernetes_pod_uid" to "01650f54-fb3c-441f-9d58-02c4c89ffda4",
          "io_kubernetes_sandbox_id" to "748e378f1cefa40d23c5efe4187197d49969b4eea49b5f2235f8e5268ae5a317",
          "server_version" to "18.06.3-ce"
        )
      ),
      nodes = TreeMap(mapOf<String, String>()),
      value = 1.0,
      timestampSeconds = DateTimeUtils.currentTimeSeconds()
    ))

    // then
    verify(exactly = 0) {
      applicationEventPublisher.publishEvent(any())
    }
  }
}
