variable "resource_group_name" {
  description = "Name of the Azure resource group"
  type        = string
  default     = "miniticket-rg"
}

variable "location" {
  description = "Azure region"
  type        = string
  default     = "eastus2"
}

variable "acr_name" {
  description = "Azure Container Registry name (globally unique)"
  type        = string
  default     = "miniticketacr12345"
}

variable "aks_cluster_name" {
  description = "AKS cluster name"
  type        = string
  default     = "miniticket-aks"
}

variable "dns_prefix" {
  description = "DNS prefix for AKS"
  type        = string
  default     = "miniticket-dns"
}

variable "node_count" {
  description = "Number of AKS nodes"
  type        = number
  default     = 2
}

variable "vm_size" {
  description = "VM size for AKS nodes"
  type        = string
  default     = "Standard_D2s_v3"
}
