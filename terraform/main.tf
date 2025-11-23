resource "aws_ecs_task_definition" "order_app" {
  family                   = "baitersburger-orders"
  cpu                      = "256"
  memory                   = "512"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = data.aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn
  container_definitions = jsonencode([{
    name      = "order-app"
    image     = "${data.aws_ecr_repository.order_app_repo.repository_url}:latest"
    essential = true
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080

    }]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = "/ecs/order-app-task-family"
        "awslogs-region"        = "us-east-1"
        "awslogs-stream-prefix" = "ecs"
      }
    }

    environment = [
      {
        name  = "TABLE_ORDER"
        value = "Orders"
      },
      {
        name  = "AWS_ACCESS_KEY"
        value = "ASIAT4KS72RODPPY32LX"
      },
      {
        name  = "AWS_SECRET_KEY"
        value = "6vnepG9xotlzpCUVA8lu+APJxAKOKNEJ30lp3/Ar"
      }
    ]

  }])

}


resource "aws_ecs_service" "order_app_service" {
  name            = "order-app-service"
  cluster         = data.aws_ecs_cluster.ecs_order_cluster.id
  task_definition = aws_ecs_task_definition.order_app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.aws_subnets_default.ids
    security_groups  = [data.aws_security_group.alb_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = data.aws_lb_target_group.order_lb_target_group.arn
    container_name   = "order-app"
    container_port   = 8080
  }

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200

  lifecycle {
    ignore_changes = [task_definition]
  }

}
