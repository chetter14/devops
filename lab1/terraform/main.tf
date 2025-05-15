terraform {
  required_providers {
    yandex = {
      source = "yandex-cloud/yandex"
    }
  }
}

provider "yandex" {
  zone      = var.zone
  token     = var.token
  folder_id = var.folder_id
  cloud_id  = var.cloud_id
}

resource "yandex_compute_disk" "boot-disk" {
  name     = var.book_disk_name
  zone     = var.zone
  image_id = data.yandex_compute_image.image.id
  size     = var.disk_size
  type     = var.disk_type
}

resource "yandex_vpc_network" "network" {
  name = var.network
}

resource "yandex_vpc_subnet" "subnet" {
  name           = var.subnet
  zone           = var.zone
  network_id     = yandex_vpc_network.network.id
  v4_cidr_blocks = var.subnet_v4_cidr_blocks
}

data "template_file" "default" {
  template = file("${path.module}/meta.yaml")
  vars = {
    ssh_public_key = file(var.ssh_key_path)
    vm_user        = var.vm_user
  }
}

data "yandex_compute_image" "image" {
  family = var.image_family
}

resource "yandex_compute_instance" "vm" {
  name        = var.name
  hostname    = var.name
  zone        = var.zone
  platform_id = var.platform_id

  resources {
    cores  = var.cores
    memory = var.memory
  }

  boot_disk {
    disk_id = yandex_compute_disk.boot-disk.id
  }

  network_interface {
    subnet_id = yandex_vpc_subnet.subnet.id
    nat       = var.nat
  }

  metadata = {
    user-data = data.template_file.default.rendered
    ssh-keys  = "${var.vm_user}:${file(var.ssh_key_path)}"
  }

  timeouts {
    create = var.timeout_create
    delete = var.timeout_delete
  }
}

output "name" {
  value = yandex_compute_instance.vm.name
}

output "address" {
  value = yandex_compute_instance.vm.network_interface.0.nat_ip_address
}
