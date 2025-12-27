
resource "aws_security_group" "ecs_sg" {
  name        = "baitersburger-ecs-sg"
  description = "Security group for ECS tasks - allows traffic from ALB"
  vpc_id      = data.aws_vpc.vpc_default.id

  # Permite tráfego do ALB na porta 8080
  ingress {
    description     = "Allow traffic from ALB on port 8080"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [data.aws_security_group.alb_sg.id]
  }

  # Permite todo tráfego de saída (para DynamoDB, ECR, etc)
  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "baitersburger-ecs-sg"
    Environment = "dev"
  }
}


resource "aws_ecs_task_definition" "order_app" {
  family                   = "baitersburger-orders"
  cpu                      = "256"
  memory                   = "512"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = data.aws_iam_role.lab_role.arn
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

    healthCheck = {
      command     = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1"]
      interval    = 30
      timeout     = 5
      retries     = 3
      startPeriod = 60
    }

    environment = [
      {
        name  = "TABLE_ORDER"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:TABLE_ORDER::"
      },
      {
        name  = "AWS_ACCESS_KEY"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:AWS_ACCESS_KEY::"
      },
      {
        name  = "AWS_SECRET_KEY"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:AWS_SECRET_KEY::"
      },
      {
        name  = "MERCADO_PAGO_ACCESS_TOKEN"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:MERCADO_PAGO_ACCESS_TOKEN::"
      },
      {
        name  = "MERCADO_PAGO_EXTERNAL_POS_ID"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:MERCADO_PAGO_EXTERNAL_POS_ID::"
      },
      {
        name  = "MERCADO_PAGO_URL"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:MERCADO_PAGO_URL::"
      },
      {
        name  = "CUSTOMER_SERVICE_URL"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:CUSTOMER_SERVICE_URL::"
      },
      {
        name  = "PRODUCT_SERVICE_URL"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:PRODUCT_SERVICE_URL::"
      },
      {
        name  = "AWS_REGION"
        value = "us-east-1"
      },
      {
        name  = "VALIDATE_SERVICE"
        value = "arn:aws:secretsmanager:us-east-1:381491842629:secret:OrdersAppSecretManager-cTpIJc:VALIDATE_SERVICE::"
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
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
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


resource "aws_cloudwatch_log_group" "order_app_logs" {
  name              = "/ecs/order-app-task-family"
  retention_in_days = 1

  tags = {
    Name        = "order-app-logs"
    Environment = "dev"
  }
}