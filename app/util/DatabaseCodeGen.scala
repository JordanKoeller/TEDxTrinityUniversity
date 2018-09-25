package util

object DatabaseCodeGen extends App {
  slick.codegen.SourceCodeGenerator.main(
      Array("slick.jdbc.PostgresProfile", "org.postgresql.Driver",
          "postgres://wlpnclrrhqlewa:5b84c825241693cc64516a7456186712d78c4eb6779aad3948d42f7298608fc0@ec2-54-235-148-19.compute-1.amazonaws.com:5432/dcegnlpg1iu06",
          "/app/app/model2",
          "model2",
          "wlpnclrrhqlewa",
          "5b84c825241693cc64516a7456186712d78c4eb6779aad3948d42f7298608fc0"
          )
      )
}