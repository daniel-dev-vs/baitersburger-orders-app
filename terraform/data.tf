data "aws_ecs_cluster" "ecs_order_cluster" {
  cluster_name = "BaitersBurgerECSCluster"
}

data "aws_iam_role" "ecs_task_execution_role" {
  name = "AWSServiceRoleForECS"
}

data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

data "aws_ecr_repository" "order_app_repo" {
  name = "baitersburger-order-app"
}

data "aws_vpc" "vpc_default" {
  default = true
}


data "aws_subnets" "aws_subnets_default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc_default.id]
  }

  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}

data "aws_security_group" "alb_sg" {
  filter {
    name   = "group-name"
    values = ["default"]
  }

  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc_default.id]
  }
}

data "aws_lb_target_group" "order_lb_target_group" {
  name = "baitersburger-alb-tg"
}