variable "zone" {
  type    = string
  default = "ru-central1-d"
}

variable "network" {
  type    = string
  default = "ya-network"
}

variable "subnet" {
  type    = string
  default = "ya-network"
}

variable "subnet_v4_cidr_blocks" {
  type    = list(string)
  default = ["192.168.10.0/24"]
}

variable "nat" {
  type    = bool
  default = true
}

variable "image_family" {
  type    = string
  default = "ubuntu-24-04-lts"
}

variable "name" {
  type    = string
  default = "itmo"
}

variable "cores" {
  type    = number
  default = 2
}

variable "memory" {
  type    = number
  default = 4
}

variable "disk_size" {
  type    = number
  default = 35
}

variable "disk_type" {
  type    = string
  default = "network-nvme"
}

variable "timeout_create" {
  default = "10m"
}

variable "timeout_delete" {
  default = "10m"
}

variable "vm_user" {
  description = "Default user for VM"
  type        = string
  default     = "ubuntu"
}

variable "folder_id" {
  type        = string
  default     = "b1gsb9vfes0n6606c3si"
}

variable "cloud_id" {
  type        = string
  default     = "b1gvtd4gjsneamatn9n7"
}

variable "token" {
  type        = string
}

variable "book_disk_name" {
  type        = string
  default = "book-disk"
}

variable "platform_id" {
  type        = string
  default = "standard-v3"
}

variable "ssh_key_path" {
  type        = string
  default = "~/.ssh/id_ed25519.pub"
}
